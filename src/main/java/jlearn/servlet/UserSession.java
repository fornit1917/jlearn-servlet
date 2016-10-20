package jlearn.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserSession
{
    private int id;

    public static UserSession loadFromRequest(HttpServletRequest req)
    {
        //todo: implement
        return new UserSession();
    }

    public static UserSession createAndStart(int userId, String authKey, HttpServletResponse resp)
    {
        //todo: implement
        return new UserSession();
    }

    public boolean isGuest()
    {
        return id != 0;
    }
}
