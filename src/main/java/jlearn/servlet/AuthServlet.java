package jlearn.servlet;

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
        switch (action) {
            case "signin":
                break;
            case "signup":
                break;
            default:
                resp.sendError(404);
        }
    }
}
