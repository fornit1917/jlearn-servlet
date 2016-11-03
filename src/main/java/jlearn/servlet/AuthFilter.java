package jlearn.servlet;

import jlearn.servlet.helper.UrlHelper;
import jlearn.servlet.service.ServiceContainer;
import jlearn.servlet.service.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AuthFilter implements Filter
{
    private UserService userService;
    private UrlHelper urlHelper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        ServiceContainer sc = (ServiceContainer) filterConfig.getServletContext().getAttribute("service-container");
        userService = sc.getUserService();
        urlHelper = new UrlHelper(filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;

        try {
            UserSession userSession = UserSession.loadFromRequest(userService, httpReq, httpResp);
            req.setAttribute("user-session", userSession);
            String action = urlHelper.getRequestUriSegment(httpReq, 1);
            switch (action) {
                case "signin":
                case "signup":
                    if (!userSession.isGuest()) {
                        httpResp.sendRedirect(urlHelper.path("/book/list"));
                        return;
                    }
                    break;
                default:
                    if (userSession.isGuest()) {
                        httpResp.sendRedirect(urlHelper.path("/signin"));
                        return;
                    }
                    break;
            }
            filterChain.doFilter(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {

    }
}
