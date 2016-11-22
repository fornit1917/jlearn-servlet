package jlearn.servlet;

import jlearn.servlet.entity.Book;
import jlearn.servlet.entity.BookStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BookServlet extends AppBaseServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String action = urlHelper.getRequestUriSegment(req, 2);
        switch (action) {
            case "list":
                doGetBookList(req, resp);
                break;
            case "add":
                doGetBookAdd(req, resp);
                break;
            default:
                resp.sendError(404);
        }
    }

    private void doGetBookList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Map<String, Object> data = new HashMap<>();
        render("book/list.ftl", data, req, resp);
    }

    private void doGetBookAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Map<String, Object> data = new HashMap<>();
        Book book = new Book();
        data.put("book", book);
        data.put("statuses", BookStatus.values());
        render("book/add.ftl", data, req, resp);
    }
}
