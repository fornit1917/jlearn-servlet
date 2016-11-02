package jlearn.servlet.service;

import jlearn.servlet.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vit on 20.10.2016.
 */
public class UserService
{
    private DataSource ds;

    public UserService(DataSource ds)
    {
        this.ds = ds;
    }

    public CommandResult<Integer> register()
    {
        return CommandResult.createOkResult();
    }

    public User authenticate(String identity, String password) throws SQLException
    {
        User user = getByEmail(identity);
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

    public User getByEmail(String email) throws SQLException
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

    private User createUserFromResultSet(ResultSet rs) throws SQLException {
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
