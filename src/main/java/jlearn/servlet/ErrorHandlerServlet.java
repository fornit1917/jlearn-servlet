package jlearn.servlet;

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
        data.put("code", code);

        if (code != 500 || !isProduction) {
            data.put("text", req.getAttribute("javax.servlet.error.message"));
        } else {
            data.put("text", "Internal server error");
        }

        if (!isProduction) {
            Throwable e = (Throwable) req.getAttribute("javax.servlet.error.exception");
            if (e != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                data.put("verbose", sw.toString());
                pw.close();
            }
        }

        //todo: log

        render("error.ftl", data, resp);
    }
}
