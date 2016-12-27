package jlearn.servlet;


import jlearn.servlet.dto.Book;
import jlearn.servlet.dto.BookDetails;
import jlearn.servlet.service.BookDetailsService;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BookDetailsServlet extends AppBaseServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        final String redirectUrl = req.getParameter("redirectUrl") != null
                ? req.getParameter("redirectUrl")
                : urlHelper.path("/book/list");
        int bookId = valueHelper.tryParseInt(req.getParameter("id"));
        int userId = getUserSession(req).getUser().getId();
        try {
            Book book = getServiceContainer().getBookService().getBookByIdAndUser(bookId, userId);
            if (book == null) {
                resp.sendError(404);
                return;
            }
            BookDetailsService service = getServiceContainer().getBookDetailsService();
            AsyncContext asyncContext = req.startAsync();
            CompletableFuture<BookDetails> result = service.loadDetails(book);
            result
                    .thenAccept(bookDetails -> {
                        try {
                            Map<String, Object> data = new HashMap<>();
                            data.put("menuBook", true);
                            data.put("book", book);
                            data.put("bookDetails", bookDetails);
                            data.put("redirectUrl", redirectUrl);
                            data.put("query", URLEncoder.encode(book.getFullName(), "Windows-1251"));
                            if (bookDetails == null) {
                                data.put("error", "not available");
                            }
                            render(
                                    "book-details/book_details.ftl",
                                    data,
                                    (HttpServletRequest) asyncContext.getRequest(),
                                    (HttpServletResponse) asyncContext.getResponse()
                            );

                        } catch (IOException | ServletException e) {

                        } finally {
                            asyncContext.complete();
                        }
                    })
                    .exceptionally(e -> {
                        try {
                            ((HttpServletResponse)asyncContext.getResponse()).sendError(500, "Error by loading book details");
                        } catch (IOException e2) {

                        } finally {
                            asyncContext.complete();
                        }
                        return null;
                    });

        } catch (SQLException e) {
            sendErrorByException(e);
            return;
        }
    }
}
