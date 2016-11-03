package jlearn.servlet;

import jlearn.servlet.entity.User;
import jlearn.servlet.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

class UserSession
{
    private User user;

    public static UserSession loadFromRequest(UserService service, HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        Cookie cookieId = null;
        Cookie cookieToken = null;
        for (Cookie c: req.getCookies()) {
            switch (c.getName()) {
                case "uid":
                    cookieId = c;
                    break;
                case "token":
                    cookieToken = c;
                    break;
            }
        }
        if (cookieId == null || cookieToken == null) {
            return new UserSession(null);
        }

        int userId;
        try {
            userId = Integer.parseInt(cookieId.getValue());
        } catch (NumberFormatException e) {
            UserSession.destroy(resp);
            return new UserSession(null);
        }

        User user = service.getById(userId);
        if (user == null || !user.isActive()) {
            UserSession.destroy(resp);
            return new UserSession(null);
        }

        if (!cookieToken.getValue().equals(UserSession.getTokenForUser(user, req.getRemoteAddr()))) {
            UserSession.destroy(resp);
            return new UserSession(null);
        }

        return new UserSession(user);
    }

    public static UserSession createAndStart(User user, String ip, boolean isRemember, HttpServletResponse resp)
    {
        Cookie cookieId = new Cookie("uid", Integer.toString(user.getId()));
        Cookie cookieToken = new Cookie("token", UserSession.getTokenForUser(user, ip));
        if (isRemember) {
            cookieId.setMaxAge(2592000);
            cookieToken.setMaxAge(2592000);
        }
        resp.addCookie(cookieId);
        resp.addCookie(cookieToken);
        return new UserSession(user);
    }

    public static void destroy(HttpServletResponse resp)
    {
        Cookie cookieId = new Cookie("uid", "");
        Cookie cookieToken = new Cookie("token", "");
        cookieId.setMaxAge(0);
        cookieToken.setMaxAge(0);
        resp.addCookie(cookieId);
        resp.addCookie(cookieToken);
    }

    private static String getTokenForUser(User user, String ip)
    {
        String s = String.valueOf(user.getId()) + user.getAuthKey() + ip;
        return DigestUtils.md5Hex(s);
    }

    private UserSession(User user)
    {
        this.user = user;
    }

    public boolean isGuest()
    {
        return user == null;
    }

    public User getUser()
    {
        return user;
    }
}
