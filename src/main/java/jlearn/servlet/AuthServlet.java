package jlearn.servlet;

import jlearn.servlet.entity.User;
import jlearn.servlet.service.CommandResult;
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
        String action = urlHelper.getRequestUriSegment(req, 1);
        Map<String, Object> data = new HashMap<>();
        String error = (String)req.getSession().getAttribute("auth-error");
        if (error != null) {
            req.getSession().removeAttribute("auth-error");
            data.put("error", error);
        }
        switch (action) {
            case "signin":
                render("auth/signin.ftl", data, req, resp);
                break;
            case "logout":
                UserSession.destroy(resp);
                resp.sendRedirect(urlHelper.path("/signin"));
                break;
            case "signup":
                String email = (String)req.getSession().getAttribute("auth-email");
                String inviteCode = (String)req.getSession().getAttribute("auth-invite");
                if (email != null) {
                    data.put("email", email);
                    req.getSession().removeAttribute("auth-email");
                }
                if (inviteCode != null) {
                    data.put("invite", inviteCode);
                    req.getSession().removeAttribute("auth-invite");
                }
                render("auth/signup.ftl", data, req, resp);
                break;
            default:
                resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = urlHelper.getRequestUriSegment(req, 1);
        switch (action) {
            case "signin":
                doPostSignIn(req, resp);
                break;
            case "signup":
                doPostSignUp(req, resp);
                break;
            default:
                resp.sendError(404);
        }
    }

    private void doPostSignIn(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        UserService service = getServiceContainer().getUserService();
        String identity = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            User user = service.authenticate(identity, password);
            if (user == null) {
                req.getSession().setAttribute("auth-error", "Incorrect email or password");
                resp.sendRedirect(urlHelper.path("/signin"));
            } else {
                UserSession.createAndStart(user, req.getRemoteAddr(), resp);
                resp.sendRedirect(urlHelper.path("/book/list"));
            }
        } catch (Exception e) {
            sendErrorByException(e);
        }
    }

    private void doPostSignUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        UserService service = getServiceContainer().getUserService();
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String passwordRepeat = req.getParameter("password_repeat");
        String inviteCode = req.getParameter("invite");
        try {
            CommandResult<User> result = service.register(email, password, passwordRepeat, inviteCode);
            if (result.isError()) {
                req.getSession().setAttribute("auth-error", result.getError().getMessage());
                req.getSession().setAttribute("auth-email", email);
                req.getSession().setAttribute("auth-invite", inviteCode);
                resp.sendRedirect(urlHelper.path("/signup"));
            } else {
                UserSession.createAndStart(result.getResult(), req.getRemoteAddr(), resp);
                resp.sendRedirect(urlHelper.path("/book/list"));
            }
        } catch (Exception e) {
            sendErrorByException(e);
        }
    }
}
