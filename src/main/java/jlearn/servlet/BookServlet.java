package jlearn.servlet;

import jlearn.servlet.dto.BookSearchCriteria;
import jlearn.servlet.entity.Book;
import jlearn.servlet.entity.BookStatus;
import jlearn.servlet.entity.User;
import jlearn.servlet.helper.ValueHelper;
import jlearn.servlet.service.utility.CommandResult;
import jlearn.servlet.service.utility.PageRequest;
import jlearn.servlet.service.utility.PageResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BookServlet extends AppBaseServlet
{
    private final static String FLASH_SUCCESSFULLY = "book-successfully";

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String action = urlHelper.getRequestUriSegment(req, 2);
        switch (action) {
            case "add":
                doPostBookAdd(req, resp);
                break;
            case "delete":
                doPostBookDelete(req, resp);
                break;
            default:
                resp.sendError(404);
        }
    }

    private void doGetBookList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Map<String, Object> data = new HashMap<>();
        int pageNum = valueHelper.tryParseInt(req.getParameter("page"));

        BookSearchCriteria criteria = new BookSearchCriteria();
        criteria.setUserId(getUserSession(req).getUser().getId());
        PageRequest pageRequest = new PageRequest(pageNum, 20);

        String adddedMessage = (String)req.getSession().getAttribute(FLASH_SUCCESSFULLY);
        if (adddedMessage != null) {
            req.getSession().removeAttribute(FLASH_SUCCESSFULLY);
            data.put("message", adddedMessage);
        }

        try {
            PageResult<Book> books = getServiceContainer().getBookService().getAll(criteria, pageRequest);
            data.put("books", books);
            data.put("criteria", criteria);
            render("book/list.ftl", data, req, resp);
        } catch (SQLException e) {
            sendErrorByException(e);
        }
    }

    private void doGetBookAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Map<String, Object> data = new HashMap<>();
        Book book = new Book();
        data.put("book", book);
        data.put("statuses", BookStatus.values());
        render("book/add.ftl", data, req, resp);
    }

    private void doPostBookAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Book book = new Book();
        book.setTitle(req.getParameter("title"));
        book.setAuthor(req.getParameter("author"));
        book.setStatus(BookStatus.getByValue(valueHelper.tryParseInt(req.getParameter("status"), 0)));
        book.setFiction(req.getParameter("is_fiction") != null);

        int userId = getUserSession(req).getUser().getId();

        try {
            CommandResult<Book> result = getServiceContainer().getBookService().addBook(userId, book);
            if (result.isError()) {
                Map<String, Object> data = new HashMap<>();
                data.put("book", book);
                data.put("error", result.getError().getMessage());
                render("book/add.ftl", data, req, resp);
            } else {
                req.getSession().setAttribute(FLASH_SUCCESSFULLY, "The Book has been added successfully");
                resp.sendRedirect(urlHelper.path("/book/list"));
            }
        } catch (SQLException e) {
            sendErrorByException(e);
        }
    }

    private void doPostBookDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        int userId = getUserSession(req).getUser().getId();
        int bookId = valueHelper.tryParseInt(req.getParameter("id"), 0);
        String redirectUrl = req.getParameter("redirectUrl");
        if (redirectUrl == null)  {
            redirectUrl = urlHelper.path("/book/list");
        }

        try {
            getServiceContainer().getBookService().deleteBook(userId, bookId);
            req.getSession().setAttribute(FLASH_SUCCESSFULLY, "Book has been deleted successfully");
            resp.sendRedirect(redirectUrl);
        } catch (SQLException e) {
            sendErrorByException(e);
        }
    }
}
