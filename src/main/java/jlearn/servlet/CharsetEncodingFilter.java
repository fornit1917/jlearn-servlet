package jlearn.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CharsetEncodingFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        servletRequest.setCharacterEncoding("UTF-8");
        if (servletRequest.getParameter("ajax") == null) {
            ((HttpServletResponse)servletResponse).addHeader("Content-Type", "text/html; charset=utf-8");
        } else {
            ((HttpServletResponse)servletResponse).addHeader("Content-Type", "application/json; charset=utf-8");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy()
    {
    }
}
