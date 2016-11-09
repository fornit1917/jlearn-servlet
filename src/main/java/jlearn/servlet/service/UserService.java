package jlearn.servlet.service;

import jlearn.servlet.entity.User;
import jlearn.servlet.helper.RandomHelper;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.DataSource;
import java.sql.*;
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

        if (password.isEmpty()) {
            return CommandResult.createErrorResult("Password required");
        }
        if (password.length() < 6) {
            return CommandResult.createErrorResult("Password must be at least 6 characters");
        }
        if (!password.equals(passwordRepeat)) {
            return CommandResult.createErrorResult("Passwords do not match");
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
            user.setHpassw(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setAuthKey(randomHelper.getRandomString(32));

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
                throw new SQLException("Unknown error. Try again later");
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
                "SELECT id, email, is_active, is_admin, auth_key, hpassw FROM \"user\" WHERE id=?"
            );
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            user = createUserFromResultSet(rs);
        }
        return user;
    }

    private User getByEmail(String email) throws SQLException
    {
        User user = null;
        try (Connection conn = ds.getConnection()) {
            PreparedStatement st = conn.prepareStatement(
                "SELECT id, email, is_active, is_admin, auth_key, hpassw FROM \"user\" WHERE email=?"
            );
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            user = createUserFromResultSet(rs);
        }
        return user;
    }

    private boolean existsUserByEmail(String email, Connection conn) throws SQLException
    {
        PreparedStatement st = conn.prepareStatement("SELECT 1 FROM \"user\" WHERE email=?");
        st.setString(1, email);
        return st.executeQuery().next();
    }

    private User createUserFromResultSet(ResultSet rs) throws SQLException
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
        }
        return user;
    }
}
