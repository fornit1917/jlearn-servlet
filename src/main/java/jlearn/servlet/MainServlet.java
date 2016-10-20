package jlearn.servlet;

import jlearn.servlet.service.UserService;
import jlearn.servlet.service.CommandResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends AppBaseServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService service = new UserService();
        CommandResult<Integer> result = service.register();
        if (result.isError()) {
            resp.getWriter().println("Error");
        } else {
            resp.getWriter().println("OK");
        }
        //resp.sendRedirect(viewHelper.path("/signin"));
    }
}
