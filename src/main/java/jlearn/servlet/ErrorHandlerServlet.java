package jlearn.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class ErrorHandlerServlet extends AppBaseServlet
{
    private static Logger logger = LoggerFactory.getLogger(ErrorHandlerServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        boolean isProduction = getServletContext().getInitParameter("env").equals("production");
        Map<String, Object> data = new HashMap<>();
        Integer code = (Integer) req.getAttribute("javax.servlet.error.status_code");
        String message = (String) req.getAttribute("javax.servlet.error.message");
        String uri = (String) req.getAttribute("javax.servlet.error.request_uri");
        data.put("code", code);

        if (code != 500 || !isProduction) {
            data.put("text", message);
        } else {
            data.put("text", "Internal server error");
        }

        Throwable e = (Throwable) req.getAttribute("javax.servlet.error.exception");
        if (!isProduction) {
            if (e != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                data.put("verbose", sw.toString());
                pw.close();
            }
        }

        if (e == null) {
            logger.error("Error {} in {} {}", code, req.getMethod().toUpperCase(), uri);
        } else {
            logger.error("Error {} in {} {}", code, req.getMethod().toUpperCase(), uri, e);
        }

        render("error.ftl", data, req, resp);
    }
}
