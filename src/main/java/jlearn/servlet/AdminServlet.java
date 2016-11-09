package jlearn.servlet;


import jlearn.servlet.AppBaseServlet;
import jlearn.servlet.entity.User;
import jlearn.servlet.service.CommandResult;
import jlearn.servlet.service.InviteService;

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String action = urlHelper.getRequestUriSegment(req, 2);
        Map<String, Object> data = new HashMap<>();
        data.put("menuAdmin", true);
        switch (action) {
            case "invite":
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
                break;
            case "user-list":
                render("admin/user-list.ftl", data, req, resp);
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
                break;
            default:
                resp.sendError(404);
                break;
        }
    }
}
