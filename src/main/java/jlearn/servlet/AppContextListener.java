package jlearn.servlet;

import freemarker.template.Configuration;
import jlearn.servlet.service.*;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
            DataSource ds = getDataSource(ctx);

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

    private DataSource getDataSource(ServletContext ctx) throws NamingException, ClassNotFoundException {
        boolean isUsingJndi = ctx.getInitParameter("use-jndi").equals("1");
        if (isUsingJndi) {
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            return (DataSource)envContext.lookup("jdbc/booksdb");
        } else {
            PoolProperties props = new PoolProperties();
            props.setDriverClassName("org.postgresql.Driver");
            props.setUrl(System.getenv("JDBC_DATABASE_URL"));
            props.setUsername(System.getenv("JDBC_DATABASE_USERNAME"));
            props.setPassword(System.getenv("JDBC_DATABASE_PASSWORD"));
            props.setMaxIdle(4);
            props.setMaxActive(10);
            props.setValidationQuery("SELECT 1");
            props.setMaxWait(10000);
            props.setJdbcInterceptors(
                    "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                    "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer"
            );
            org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
            dataSource.setPoolProperties(props);
            return dataSource;
        }
    }
}
