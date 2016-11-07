package jlearn.servlet;

import jlearn.servlet.helper.UrlHelper;
import jlearn.servlet.service.ServiceContainer;
import jlearn.servlet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        String action = urlHelper.getRequestUriSegment(httpReq, 1);
        if (action.equals("static")) {
            filterChain.doFilter(req, resp);
            return;
        }

        try {
            UserSession userSession = UserSession.loadFromRequest(userService, httpReq, httpResp);
            req.setAttribute("user-session", userSession);
            String uri = httpReq.getRequestURI();
            String root = httpReq.getContextPath() + "/";
            if (userSession.isGuest()) {
                if (uri.equals(root) || action.equals("logout")) {
                    httpResp.sendRedirect(urlHelper.path("/signin"));
                } else if (action.equals("signin") || action.equals("signup")) {
                    filterChain.doFilter(req, resp);
                } else {
                    httpResp.sendError(404);
                }
            } else {
                filterChain.doFilter(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {

    }
}
