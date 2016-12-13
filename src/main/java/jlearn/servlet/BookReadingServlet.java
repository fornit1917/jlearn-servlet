package jlearn.servlet;

import jlearn.servlet.dto.BookReadingHistoryItem;
import jlearn.servlet.service.BookReadingService;
import jlearn.servlet.service.utility.PageRequest;
import jlearn.servlet.service.utility.PageResult;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class BookReadingServlet extends AppBaseServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String action = urlHelper.getRequestUriSegment(req, 2);
        switch (action) {
            case "history":
                doGetHistory(req, resp);
                break;
            default:
                resp.sendError(404);
        }
    }

    private void doGetHistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        int pageNum = valueHelper.tryParseInt(req.getParameter("page"), 0);
        int userId = getUserSession(req).getUser().getId();
        boolean ajax = req.getParameter("ajax") != null;
        PageRequest pageRequest = new PageRequest(pageNum, 20);
        BookReadingService bookReadingService = getServiceContainer().getBookReadingService();
        try {
            PageResult<BookReadingHistoryItem> history = bookReadingService.getListForHistory(userId, pageRequest);
            JSONObject result = getHistoryAsJson(history);
            Map<String, Object> data = new HashMap<>();
            data.put("json", result.toJSONString());
            if (ajax) {
                result.writeJSONString(resp.getWriter());
            } else {
                render("book-reading/history.ftl", data, req, resp);
            }
        } catch (SQLException e) {
            sendErrorByException(e);
        }

    }

    private JSONObject getHistoryAsJson(PageResult<BookReadingHistoryItem> history)
    {
        JSONObject json = new JSONObject();
        json.put("pageNum", history.getPageNum());
        json.put("hasNextPage", history.hasNext());

        List<Map<String, Object>> data = new ArrayList<>(history.getSlice().size());
        for (BookReadingHistoryItem item : history.getSlice()) {
            data.add(item.toMap());
        }
        json.put("data", data);

        return  json;
    }
}
