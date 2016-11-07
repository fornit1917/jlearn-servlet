package jlearn.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            resp.sendRedirect(urlHelper.path("/book/list"));
        } else {
            resp.sendError(404);
        }
    }
}
