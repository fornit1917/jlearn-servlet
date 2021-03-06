package jlearn.servlet;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jlearn.servlet.helper.UrlHelper;
import jlearn.servlet.helper.ValueHelper;
import jlearn.servlet.service.ServiceContainer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class AppBaseServlet extends HttpServlet
{
    protected UrlHelper urlHelper;
    protected ValueHelper valueHelper;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        urlHelper = new UrlHelper(getServletContext());
        valueHelper = new ValueHelper();
    }

    protected void render(String templateName, Map<String, Object> data, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        data.put("urlHelper", urlHelper);
        data.put("valueHelper", valueHelper);
        data.put("user", getUserSession(req).getUser());
        if (req.getQueryString() == null) {
            data.put("requestUrl", req.getRequestURI());
        } else {
            data.put("requestUrl", req.getRequestURI() + "?" + req.getQueryString());
        }

        try {
            Template template = getServiceContainer().getTemplatesConfig().getTemplate(templateName);
            template.process(data, resp.getWriter());
        } catch (IOException | TemplateException e) {
            sendErrorByException(e);
        }
    }

    protected ServiceContainer getServiceContainer() throws ServletException
    {
        ServiceContainer sc = (ServiceContainer)getServletContext().getAttribute("service-container");
        if (sc == null) {
            throw new ServletException("Cannot get service container");
        }
        return sc;
    }

    protected void sendErrorByException(Throwable e) throws ServletException
    {
        throw new ServletException(e);
    }

    protected UserSession getUserSession(HttpServletRequest req) throws ServletException
    {
        UserSession userSession = (UserSession) req.getAttribute("user-session");
        if (userSession == null) {
            throw new ServletException("User session has not loaded");
        }
        return userSession;
    }
}
