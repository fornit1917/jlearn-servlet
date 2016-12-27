package jlearn.servlet;

import freemarker.template.Configuration;
import jlearn.servlet.service.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)envContext.lookup("jdbc/booksdb");

            ServiceContainer sc = new ServiceContainer();

            InviteService inviteService = new InviteService(ds);
            sc.setInviteService(inviteService);

            UserService userService = new UserService(ds);
            userService.setInviteService(inviteService);
            sc.setUserService(userService);

            BookReadingService bookReadingService = new BookReadingService(ds);
            sc.setBookReadingService(bookReadingService);

            BookService bookService = new BookService(ds);
            bookService.setBookReadingService(bookReadingService);
            sc.setBookService(bookService);

            BookDetailsService bookDetailsService = new BookDetailsService();
            sc.setBookDetailsService(bookDetailsService);

            Configuration templateConfig = new Configuration(Configuration.VERSION_2_3_25);
            try {
                templateConfig.setDirectoryForTemplateLoading(new File(ctx.getRealPath("WEB-INF/templates")));
            } catch (IOException e) {
                throw new ServletException(e);
            }
            sc.setTemplatesConfig(templateConfig);

            ctx.setAttribute("service-container", sc);
        } catch (Exception e) {
            //todo: log
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
