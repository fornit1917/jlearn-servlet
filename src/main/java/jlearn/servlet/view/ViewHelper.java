package jlearn.servlet.view;

import javax.servlet.ServletContext;

public class ViewHelper
{
    private ServletContext context;

    public ViewHelper(ServletContext context)
    {
        this.context = context;
    }

    public String path(String path)
    {
        return context.getContextPath() + path;
    }
}
