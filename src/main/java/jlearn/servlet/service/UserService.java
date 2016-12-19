package jlearn.servlet.service;

import jlearn.servlet.dto.UserSearchCriteria;
import jlearn.servlet.dto.User;
import jlearn.servlet.exception.NotFoundException;
import jlearn.servlet.helper.RandomHelper;
import jlearn.servlet.service.utility.CommandResult;
import jlearn.servlet.service.utility.PageRequest;
import jlearn.servlet.service.utility.PageResult;
import jlearn.servlet.service.utility.QueryBuilder;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService
{
    private DataSource ds;
    private InviteService inviteService;
    private Pattern emailRegex;
    private RandomHelper randomHelper;

    public UserService(DataSource ds)
    {
        this.ds = ds;
        emailRegex = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        randomHelper = new RandomHelper();
    }

    public void setInviteService(InviteService inviteService)
    {
        this.inviteService = inviteService;
    }

    public CommandResult<User> register(String email, String password, String passwordRepeat, String inviteCode) throws SQLException
    {
        email = email.trim().toLowerCase();
        inviteCode = inviteCode.trim();
        if (email.isEmpty()) {
            return CommandResult.createErrorResult("Email is required");
        }

        Matcher matcher = emailRegex.matcher(email);
        if (!matcher.matches()) {
            return CommandResult.createErrorResult("Email is not valid");
        }

        CommandResult validatePasswordResult = validatePassword(password, passwordRepeat);
        if (validatePasswordResult.isError()) {
            return validatePasswordResult;
        }

        try (Connection conn = ds.getConnection()) {
            if (existsUserByEmail(email, conn)) {
                return CommandResult.createErrorResult("Email is already used");
            }

            Integer inviteId = inviteService.getInviteIdIfFree(inviteCode, conn);
            if (inviteId == null) {
                return CommandResult.createErrorResult("Incorrect or busy invite code");
            }

            User user = new User();
            user.setEmail(email);
            user.setActive(true);
            user.setAdmin(false);
            setPasswordAndAuthKeyToDto(user, password);

            PreparedStatement st = conn.prepareStatement(
                "INSERT INTO \"user\" (email, is_active, is_admin, hpassw, auth_key, invite_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, email);
            st.setBoolean(2, user.isActive());
            st.setBoolean(3, user.isAdmin());
            st.setString(4, user.getHpassw());
            st.setString(5, user.getAuthKey());
            st.setInt(6, inviteId);
            try {
                st.executeUpdate();
            } catch (SQLException e) {
                throw new SQLException("Unknown error. Try again later", e);
            }

            ResultSet ids = st.getGeneratedKeys();
            if (ids.next()) {
                user.setId(ids.getInt(1));
            } else {
                throw new SQLException("Cannot generate id for new user");
            }

            return CommandResult.createOkResult(user);
        }
    }

    public User authenticate(String identity, String password) throws SQLException
    {
        User user = getByEmail(identity.toLowerCase());
        if (user == null || !user.isActive()) {
            return null;
        }
        if (!BCrypt.checkpw(password, user.getHpassw())) {
            return null;
        }
        return user;
    }

    public User getById(int userId) throws SQLException
    {
        User user = null;
        try (Connection conn = ds.getConnection()) {
            PreparedStatement st = conn.prepareStatement(
                "SELECT id, email, is_active, is_public, is_admin, auth_key, hpassw, create_date FROM \"user\" WHERE id=? AND is_active=?"
            );
            st.setInt(1, userId);
            st.setBoolean(2, true);
            ResultSet rs = st.executeQuery();
            user = createOneUserFromResultSet(rs);
        }
        return user;
    }

    public PageResult<User> getAll(UserSearchCriteria criteria, PageRequest pageRequest) throws SQLException
    {
        String email = criteria.getEmail();
        int state = criteria.getState();
        QueryBuilder queryBuilder = new QueryBuilder().table("\"user\"");
        if (email != null && !email.isEmpty()) {
            queryBuilder.andWhere("email LIKE ?", "%" + email.toLowerCase() + "%");
        }
        if (state == UserSearchCriteria.STATE_ACTIVE) {
            queryBuilder.andWhere("is_active = ?", true);
        } else if (state == UserSearchCriteria.STATE_INACTIVE) {
            queryBuilder.andWhere("is_active = ?", false);
        }
        if (criteria.isOnlyPublic()) {
            queryBuilder.andWhere("is_public = ?", true);
        }
        try (Connection conn = ds.getConnection()) {
            //get total count
            PreparedStatement ps = queryBuilder.selectCount().createPreparedStatement(conn);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int totalCount = rs.getInt(1);

            //get records
            ps = queryBuilder.selectColumns("id", "email", "is_active", "is_admin", "create_date")
                .limit(pageRequest.getPageSize())
                .offset(pageRequest.getOffset())
                .createPreparedStatement(conn);
            rs = ps.executeQuery();
            List<User> users = new ArrayList<>(pageRequest.getPageSize());
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setActive(rs.getBoolean("is_active"));
                user.setAdmin(rs.getBoolean("is_admin"));
                user.setCreateDate(rs.getDate("create_date"));
                users.add(user);
            }

            return new PageResult<>(users, totalCount, pageRequest);
        }
    }

    public PageResult<User> getPublicUsers(UserSearchCriteria criteria, PageRequest pageRequest) throws SQLException
    {
        criteria.setOnlyPublic(true);
        criteria.setState(UserSearchCriteria.STATE_ACTIVE);
        return getAll(criteria, pageRequest);
    }

    public void setActive(int userId, boolean isActive) throws SQLException
    {
        try (Connection conn = ds.getConnection()) {
            PreparedStatement st = conn.prepareStatement("UPDATE \"user\" SET is_active=? WHERE id=?");
            st.setBoolean(1, isActive);
            st.setInt(2, userId);
            st.executeUpdate();
        }
    }

    public CommandResult<User> editProfile(int userId, String password, String passwordRepeat, boolean isPublic) throws SQLException, NotFoundException
    {
        User user = getById(userId);
        if (user == null) {
            throw new NotFoundException();
        }
        if (password != null && !password.isEmpty()) {
            CommandResult validatePasswordResult = validatePassword(password, passwordRepeat);
            if (validatePasswordResult.isError()) {
                return validatePasswordResult;
            }
            setPasswordAndAuthKeyToDto(user, password);
        }
        user.setPublic(isPublic);
        try (Connection conn = ds.getConnection()) {
            String sql = "UPDATE \"user\" SET hpassw=?, auth_key=?, is_public=? WHERE id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, user.getHpassw());
            st.setString(2, user.getAuthKey());;
            st.setBoolean(3, user.isPublic());
            st.setInt(4, userId);
            st.executeUpdate();
            return CommandResult.createOkResult(user);
        }
    }

    private CommandResult<User> validatePassword(String password, String passwordRepeat)
    {
        if (password == null || password.isEmpty()) {
            return CommandResult.createErrorResult("Password required");
        }
        if (password.length() < 6) {
            return CommandResult.createErrorResult("Password must be at least 6 characters");
        }
        if (!password.equals(passwordRepeat)) {
            return CommandResult.createErrorResult("Passwords do not match");
        }
        return CommandResult.createOkResult();
    }

    private User getByEmail(String email) throws SQLException
    {
        User user = null;
        try (Connection conn = ds.getConnection()) {
            PreparedStatement st = conn.prepareStatement(
                "SELECT id, email, is_active, is_admin, is_public, auth_key, hpassw, create_date FROM \"user\" WHERE email=? AND is_active=?"
            );
            st.setString(1, email);
            st.setBoolean(2, true);
            ResultSet rs = st.executeQuery();
            user = createOneUserFromResultSet(rs);
        }
        return user;
    }

    private boolean existsUserByEmail(String email, Connection conn) throws SQLException
    {
        PreparedStatement st = conn.prepareStatement("SELECT 1 FROM \"user\" WHERE email=?");
        st.setString(1, email);
        return st.executeQuery().next();
    }

    private User createOneUserFromResultSet(ResultSet rs) throws SQLException
    {
        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setAdmin(rs.getBoolean("is_admin"));
            user.setAuthKey(rs.getString("auth_key"));
            user.setActive(rs.getBoolean("is_active"));
            user.setHpassw(rs.getString("hpassw"));
            user.setPublic(rs.getBoolean("is_public"));
        }
        return user;
    }

    private void setPasswordAndAuthKeyToDto(User user, String password)
    {
        user.setHpassw(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setAuthKey(randomHelper.getRandomString(32));
    }
}
