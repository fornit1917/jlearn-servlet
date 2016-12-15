package jlearn.servlet;

import jlearn.servlet.dto.User;
import jlearn.servlet.exception.NotFoundException;
import jlearn.servlet.service.UserService;
import jlearn.servlet.service.utility.CommandResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends AppBaseServlet
{
    private static final String FLASH_ERROR = "profile-error";
    private static final String FLASH_SUCCESS = "profile-success";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String action = urlHelper.getRequestUriSegment(req, 2);
        switch (action) {
            case "profile":
                doGetProfile(req, resp);
                break;
            default:
                resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String action = urlHelper.getRequestUriSegment(req, 2);
        switch (action) {
            case "profile":
                doPostProfile(req, resp);
                break;
            default:
                resp.sendError(404);
        }
    }

    private void doGetProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        User editableUser = getUserSession(req).getUser();
        Map<String, Object> data = new HashMap<>();
        data.put("editableUser", editableUser);

        String success = (String) req.getSession().getAttribute(FLASH_SUCCESS);
        if (success != null) {
            req.getSession().removeAttribute(FLASH_SUCCESS);
            data.put("success", success);
        } else {
            String error = (String) req.getSession().getAttribute(FLASH_ERROR);
            if (error != null) {
                req.getSession().removeAttribute(FLASH_ERROR);
                data.put("error", error);
            }
        }
        render("user/profile.ftl", data, req, resp);
    }

    private void doPostProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        int userId = getUserSession(req).getUser().getId();
        String password = req.getParameter("password");
        String passwordRepeat = req.getParameter("password_repeat");
        boolean isPublic = req.getParameter("is_public") != null;
        UserService userService = getServiceContainer().getUserService();
        try {
            CommandResult<User> result = userService.editProfile(userId, password, passwordRepeat, isPublic);
            if (result.isError()) {
                req.getSession().setAttribute(FLASH_ERROR, result.getError().getMessage());
            } else {
                req.getSession().setAttribute(FLASH_SUCCESS, "The Profile has been updated successfully");
                UserSession.updateToken(result.getResult(), req, resp);
            }
            resp.sendRedirect(urlHelper.path("/user/profile"));
        } catch (SQLException e) {
            sendErrorByException(e);
        } catch (NotFoundException e) {
            resp.sendError(404);
        }
    }
}
