package jlearn.servlet.helper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class UrlHelper
{
    private ServletContext context;

    public UrlHelper(ServletContext context)
    {
        this.context = context;
    }

    public String path(String path)
    {
        return context.getContextPath() + path;
    }

    public String getRequestUriSegment(HttpServletRequest req, int num)
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

    public ParsedUrl parseUrl(String url)
    {
        return new ParsedUrl(url);
    }
}
