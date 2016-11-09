package jlearn.servlet.service;

import jlearn.servlet.helper.RandomHelper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InviteService
{
    private DataSource ds;
    private RandomHelper randomHelper;

    public InviteService(DataSource ds)
    {
        this.ds = ds;
        this.randomHelper = new RandomHelper();
    }

    public Integer getInviteIdIfFree(String code, Connection conn) throws SQLException
    {
        String sql = "SELECT i.id FROM invite as i LEFT JOIN \"user\" as u ON (u.invite_id=i.id) " +
                "WHERE i.code=? AND u.id IS NULL";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, code);
        ResultSet rs = st.executeQuery();
        Integer inviteId = rs.next() ? rs.getInt(1) : null;
        rs.close();
        return inviteId;
    }

    public CommandResult<String> createInviteCode(String code) throws SQLException
    {
        if (code == null || code.isEmpty()) {
            code = randomHelper.getRandomString(64);
        }

        try (Connection conn = ds.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM invite WHERE code=?");
            ps.setString(1, code);
            if (ps.executeQuery().next()) {
                return CommandResult.createErrorResult("This code already exists");
            }

            ps = conn.prepareStatement("INSERT INTO invite (code) VALUES (?)");
            ps.setString(1, code);
            try {
                int rowCount = ps.executeUpdate();
                if (rowCount > 0) {
                    return CommandResult.createOkResult(code);
                } else {
                    return CommandResult.createErrorResult("Unknown error. Try again");
                }
            } catch (SQLException e) {
                return CommandResult.createErrorResult("Unknown error. Try again");
            }
        }
    }
}
