package jlearn.servlet;


import jlearn.servlet.dto.UserSearchCriteria;
import jlearn.servlet.dto.User;
import jlearn.servlet.exception.NotFoundException;
import jlearn.servlet.service.UserService;
import jlearn.servlet.service.utility.CommandResult;
import jlearn.servlet.service.utility.PageRequest;
import jlearn.servlet.service.utility.PageResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends AppBaseServlet
{
    private static final String FLASH_ADDED_INVITE_CODE = "added-invite-code";
    private static final String FLASH_ADD_INVITE_ERROR = "add-invite-error";

    private static final String FLASH_EDIT_USER_SUCCESS = "admin-edit-user-success";
    private static final String FLASH_EDIT_USER_ERROR = "admin-flash-edit-user-error";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String action = urlHelper.getRequestUriSegment(req, 2);
        switch (action) {
            case "invite":
                doGetInvite(req, resp);
                break;
            case "user-list":
                doGetUserList(req, resp);
                break;
            case "user-edit":
                doGetUserEdit(req, resp);
                break;
            default:
                resp.sendError(404);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String action = urlHelper.getRequestUriSegment(req, 2);
        switch (action) {
            case "invite":
                doPostInvite(req, resp);
                break;
            case "user-set-active":
                doPostUserSetActive(req, resp);
                break;
            case "user-edit":
                doPostUserEdit(req, resp);
                break;
            default:
                resp.sendError(404);
                break;
        }
    }

    private void doGetInvite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Map<String, Object> data = new HashMap<>();
        data.put("menuAdmin", true);
        data.put("menuAdminInvite", true);
        String addedCode = (String) req.getSession().getAttribute(FLASH_ADDED_INVITE_CODE);
        if (addedCode != null) {
            data.put("addedCode", addedCode);
            req.getSession().removeAttribute(FLASH_ADDED_INVITE_CODE);
        }
        String error = (String) req.getSession().getAttribute(FLASH_ADD_INVITE_ERROR);
        if (error != null) {
            data.put("error", error);
            req.getSession().removeAttribute(FLASH_ADD_INVITE_ERROR);
        }
        render("admin/invite.ftl", data, req, resp);
    }

    private void doPostInvite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String code = req.getParameter("code").trim();
        try {
            CommandResult<String> result = getServiceContainer().getInviteService().createInviteCode(code);
            if (result.isError()) {
                req.getSession().setAttribute(FLASH_ADD_INVITE_ERROR, result.getError().getMessage());
            } else {
                req.getSession().setAttribute(FLASH_ADDED_INVITE_CODE, result.getResult());
            }
            resp.sendRedirect(urlHelper.path("/admin/invite"));
        } catch (SQLException e) {
            sendErrorByException(e);
        }
    }

    private void doGetUserList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Map<String, Object> data = new HashMap<>();
        data.put("menuAdmin", true);
        data.put("menuAdminUser", true);

        String email = req.getParameter("email");
        int pageNum = valueHelper.tryParseInt(req.getParameter("page"));
        int state = valueHelper.tryParseInt(req.getParameter("state"), UserSearchCriteria.STATE_ALL);
        String message = (String) req.getSession().getAttribute(FLASH_EDIT_USER_SUCCESS);
        if (message != null) {
            data.put("message", message);
            req.getSession().removeAttribute(FLASH_EDIT_USER_SUCCESS);
        }
        UserSearchCriteria criteria = new UserSearchCriteria(email, state);
        PageRequest pageRequest = new PageRequest(pageNum, 20);
        try {
            PageResult<User> users = getServiceContainer().getUserService().getAll(criteria, pageRequest);
            data.put("users", users);
            data.put("criteria", criteria);
            render("admin/user_list.ftl", data, req, resp);
        } catch(SQLException e) {
            sendErrorByException(e);
        }
    }

    private void doPostUserSetActive(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        int userId = valueHelper.tryParseInt(req.getParameter("id"));
        boolean isActive = req.getParameter("active") != null && req.getParameter("active").equals("1");
        String redirectUrl = req.getParameter("redirectUrl") != null ? req.getParameter("redirectUrl") : urlHelper.path("/admin/user-list");
        try {
            getServiceContainer().getUserService().setActive(userId, isActive);
            resp.sendRedirect(redirectUrl);
        } catch (SQLException e) {
            sendErrorByException(e);
        }
    }

    private void doGetUserEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        int userId = valueHelper.tryParseInt(req.getParameter("id"));
        String redirectUrl = req.getParameter("redirectUrl");
        if (redirectUrl == null) {
            redirectUrl = urlHelper.path("admin/user-list");
        }
        UserService userService = getServiceContainer().getUserService();
        try {
            User editableUser = userService.getById(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("editableUser", editableUser);
            data.put("redirectUrl", redirectUrl);

            String error = (String) req.getSession().getAttribute(FLASH_EDIT_USER_ERROR);
            if (error != null) {
                data.put("error", error);
                req.getSession().removeAttribute(FLASH_EDIT_USER_ERROR);
            }

            render("admin/user_edit.ftl", data, req, resp);
        } catch (SQLException e) {
            sendErrorByException(e);
        }

    }

    private void doPostUserEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        UserService userService = getServiceContainer().getUserService();
        int userId = valueHelper.tryParseInt(req.getParameter("id"));
        String password = req.getParameter("password");
        String passwordRepeat = req.getParameter("password_repeat");
        String redirectUrl = req.getParameter("redirectUrl");
        boolean isPublic = req.getParameter("is_public") != null;

        try {
            CommandResult<User> result = userService.editProfile(userId, password, passwordRepeat, isPublic);
            if (result.isError()) {
                req.getSession().setAttribute(FLASH_EDIT_USER_ERROR, result.getError().getMessage());
                resp.sendRedirect(urlHelper.path("/admin/user-edit?redirectUrl=" + redirectUrl + "&id=" + userId));
            } else {
                req.getSession().setAttribute(FLASH_EDIT_USER_SUCCESS, "The Profile has been updated successfully");
                resp.sendRedirect(redirectUrl);
            }
        } catch (SQLException e) {
            sendErrorByException(e);
        } catch (NotFoundException e) {
            resp.sendError(404);
        }
    }
}
