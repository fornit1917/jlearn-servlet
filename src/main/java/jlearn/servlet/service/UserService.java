package jlearn.servlet.service;

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

    public boolean authenticate(String identity, String password)
    {
        return false;
    }
}
