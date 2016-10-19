package jlearn.servlet;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jlearn.servlet.view.ViewHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class AppBaseServlet extends HttpServlet
{
    private Configuration templateConfig;

    protected ViewHelper viewHelper;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        viewHelper = new ViewHelper(getServletContext());
        templateConfig = new Configuration(Configuration.VERSION_2_3_25);
        try {
            templateConfig.setDirectoryForTemplateLoading(new File(getServletContext().getRealPath("templates")));
        } catch (IOException e) {
            //todo: handling
            e.printStackTrace();
        }
    }

    protected void render(String templateName, Map<String, Object> data, HttpServletResponse resp)
    {
        data.put("helper", viewHelper);
        try {
            Template template = templateConfig.getTemplate(templateName);
            template.process(data, resp.getWriter());
        } catch (IOException | TemplateException e) {
            //todo: handling
            e.printStackTrace();
        }
    }

    protected String getRequestUriSegment(HttpServletRequest req, int num)
    {
        String uri;
        String prefix = req.getContextPath();
        if (prefix != null && !prefix.isEmpty()) {
            uri = req.getRequestURI().substring(prefix.length());
        } else {
            uri = req.getRequestURI();
        }

        String[] parts = uri.split("/");
        return parts.length > num ? parts[num] : "";
    }
}
