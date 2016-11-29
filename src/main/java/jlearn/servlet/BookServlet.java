package jlearn.servlet;

import jlearn.servlet.dto.BookSearchCriteria;
import jlearn.servlet.entity.Book;
import jlearn.servlet.entity.BookStatus;
import jlearn.servlet.service.utility.CommandResult;
import jlearn.servlet.service.utility.PageRequest;
import jlearn.servlet.service.utility.PageResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BookServlet extends AppBaseServlet
{
    private final static String FLASH_SUCCESSFULLY = "book-successfully";
    private final static String FLASH_ERROR = "book-error";

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
            case "update":
                doGetBookUpdate(req, resp);
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
            case "update":
                doPostBookUpdate(req, resp);
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
        criteria.setTitle(req.getParameter("title"));
        criteria.setAuthor(req.getParameter("author"));
        criteria.setType(valueHelper.tryParseInt(req.getParameter("type"), BookSearchCriteria.TYPE_ALL));
        criteria.setStatus(valueHelper.tryParseInt(req.getParameter("status"), BookSearchCriteria.TYPE_ALL));

        PageRequest pageRequest = new PageRequest(pageNum, 20);

        String addedMessage = (String)req.getSession().getAttribute(FLASH_SUCCESSFULLY);
        if (addedMessage != null) {
            req.getSession().removeAttribute(FLASH_SUCCESSFULLY);
            data.put("message", addedMessage);
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
        popupateBookByRequest(req, book);

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

    private void doGetBookUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        int userId = getUserSession(req).getUser().getId();
        int bookId = valueHelper.tryParseInt(req.getParameter("id"), 0);
        Map<String, Object> data = new HashMap<>();
        data.put("statuses", BookStatus.values());
        try {
            Book book = getServiceContainer().getBookService().getBookByIdAndUser(bookId, userId);
            if (book == null) {
                resp.sendError(404);
                return;
            }
            data.put("book", book);
            render("book/update.ftl", data, req, resp);
        } catch (SQLException e) {
            sendErrorByException(e);
        }
    }

    private void doPostBookUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        int userId = getUserSession(req).getUser().getId();
        int bookId = valueHelper.tryParseInt(req.getParameter("id"), 0);
        Map<String, Object> data = new HashMap<>();
        data.put("statuses", BookStatus.values());

        try {
            Book book = getServiceContainer().getBookService().getBookByIdAndUser(bookId, userId);
            if (book == null) {
                resp.sendError(404);
                return;
            }
            data.put("book", book);
            popupateBookByRequest(req, book);
            CommandResult<Book> result = getServiceContainer().getBookService().editBook(book);
            if (result.isError()) {
                data.put("error", result.getError().getMessage());
            } else {
                data.put("success", true);
            }
            render("book/update.ftl", data, req, resp);

        } catch (SQLException e) {
            sendErrorByException(e);
        }
    }

    private void popupateBookByRequest(HttpServletRequest req, Book book)
    {
        book.setTitle(req.getParameter("title"));
        book.setAuthor(req.getParameter("author"));
        book.setStatus(BookStatus.getByValue(valueHelper.tryParseInt(req.getParameter("status"), 0)));
        book.setFiction(req.getParameter("is_fiction") != null);
    }

}
