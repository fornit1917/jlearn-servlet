package jlearn.servlet.service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InviteService
{
    private DataSource ds;

    public InviteService(DataSource ds)
    {
        this.ds = ds;
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
}
