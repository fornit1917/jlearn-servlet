package jlearn.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends AppBaseServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String root = req.getContextPath() + "/";
        if (uri.equals(root)) {
            if (getUserSession(req).isGuest()) {
                resp.sendRedirect(urlHelper.path("/signin"));
            } else {
                resp.sendRedirect(urlHelper.path("/book/list"));
            }

        } else {
            resp.sendError(404);
        }
    }
}
