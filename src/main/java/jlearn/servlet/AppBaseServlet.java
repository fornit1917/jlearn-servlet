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
import java.util.Map;

public class AppBaseServlet extends HttpServlet
{
    private Configuration templateConfig;

    protected UrlHelper urlHelper;
    protected ValueHelper valueHelper;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        urlHelper = new UrlHelper(getServletContext());
        valueHelper = new ValueHelper();
        templateConfig = new Configuration(Configuration.VERSION_2_3_25);
        try {
            templateConfig.setDirectoryForTemplateLoading(new File(getServletContext().getRealPath("WEB-INF/templates")));
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    protected void render(String templateName, Map<String, Object> data, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        data.put("urlHelper", urlHelper);
        if (req != null) {
            data.put("user", getUserSession(req).getUser());
        }
        try {
            Template template = templateConfig.getTemplate(templateName);
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
