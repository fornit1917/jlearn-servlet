package jlearn.servlet;

import jlearn.servlet.service.ServiceContainer;
import jlearn.servlet.service.UserService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.sql.DataSource;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)envContext.lookup("jdbc/booksdb");

            ServiceContainer sc = new ServiceContainer();
            sc.setUserService(new UserService(ds));

            ctx.setAttribute("service-container", sc);
        } catch (Exception e) {
            //todo: log
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
