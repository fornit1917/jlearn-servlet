package jlearn.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class BookServlet extends AppBaseServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        UserSession userSession = getUserSession(req);
        if (userSession.isGuest()) {
            resp.getWriter().write("Guest");
        } else {
            resp.getWriter().write(userSession.getUser().getEmail());
        }
    }
}
