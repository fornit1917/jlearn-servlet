package jlearn.servlet;

import jlearn.servlet.entity.User;
import jlearn.servlet.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthServlet extends AppBaseServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getRequestUriSegment(req, 1);
        Map<String, Object> data = new HashMap<>();
        String error = (String)req.getSession().getAttribute("auth-error");
        if (error != null) {
            req.getSession().removeAttribute("auth-error");
            data.put("error", error);
        }
        switch (action) {
            case "signin":
                render("auth/signin.ftl", data, resp);
                break;
            case "logout":
                break;
            case "signup":
                render("auth/signup.ftl", data, resp);
                break;
            default:
                resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getRequestUriSegment(req, 1);
        UserService service = new UserService(getDataSource());
        switch (action) {
            case "signin":
                String identity = req.getParameter("email");
                String password = req.getParameter("password");
                try {
                    User user = service.authenticate(identity, password);
                    if (user == null) {
                        req.getSession().setAttribute("auth-error", "Incorrect email or password");
                        resp.sendRedirect(viewHelper.path("/signin"));
                    } else {
                        UserSession.createAndStart(user, req.getRemoteAddr(), false, resp);
                        resp.sendRedirect(viewHelper.path("/book/list"));
                    }
                } catch (Exception e) {
                    sendErrorByException(e);
                }
                break;

            case "signup":
                break;

            default:
                resp.sendError(404);
        }
    }
}
