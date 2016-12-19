package jlearn.servlet.service;

import jlearn.servlet.dto.Book;
import jlearn.servlet.dto.BookReading;
import jlearn.servlet.dto.BookReadingHistoryItem;
import jlearn.servlet.dto.BookStatus;
import jlearn.servlet.helper.ValueHelper;
import jlearn.servlet.service.utility.ErrorDescriptor;
import jlearn.servlet.service.utility.PageRequest;
import jlearn.servlet.service.utility.PageResult;
import jlearn.servlet.service.utility.QueryBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookReadingService
{
    private DataSource ds;
    private ValueHelper valueHelper;

    public BookReadingService(DataSource ds)
    {
        this.ds = ds;
        this.valueHelper = new ValueHelper();
    }

    public ErrorDescriptor validateBookReading(BookReading bookReading)
    {
        if (bookReading.getStartYear() < 0 || bookReading.getEndYear() < 0
                || bookReading.getStartMonth() < 0 || bookReading.getEndYear() < 0)
        {
            return new ErrorDescriptor("Incorrect values for period");
        }

        if (bookReading.getStartYear() > 0 && bookReading.getEndYear() > 0) {
            if (bookReading.getStartYear() > bookReading.getEndYear()) {
                return new ErrorDescriptor("Start year must be not more than end year");
            }
        }

        if (bookReading.getStartMonth() > 0 && bookReading.getEndMonth() > 0
                && bookReading.getStartYear() > 0 && bookReading.getStartYear() == bookReading.getEndYear())
        {
            if (bookReading.getStartMonth() > bookReading.getEndMonth()) {
                return new ErrorDescriptor("Start month must be not more than end month");
            }
        }

        return null;
    }

    public void addBookReadingForBook(Book book, BookReading bookReading, Connection conn) throws SQLException
    {
        String sql = "INSERT INTO book_reading (book_id, status, start_year, start_month, end_year, end_month, is_reread) " +
                "VALUES (?,?,?,?,?,?,?)";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, book.getId());
        st.setInt(2, bookReading.getStatus().getValue());
        st.setInt(3, bookReading.getStartYear());
        st.setInt(4, bookReading.getStartMonth());
        if (book.getStatus() == BookStatus.IN_PROGRESS) {
            st.setInt(5, 0);
            st.setInt(6, 0);
        } else {
            st.setInt(5, bookReading.getEndYear());
            st.setInt(6, bookReading.getEndMonth());
        }
        st.setBoolean(7, bookReading.isReread());

        st.executeUpdate();
        st.close();
    }

    public void updateLastBookReadingForBook(Book book, BookReading newBookReadingData, Connection conn) throws SQLException
    {
        BookReading lastBookReading = getBookReadingInfoForBook(book, conn);
        if (lastBookReading.getId() == 0) {
            addBookReadingForBook(book, newBookReadingData, conn);
        } else {
            String sql = "UPDATE book_reading SET status=?, start_year=?, start_month=?, end_year=?, end_month=?, is_reread=? WHERE id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, newBookReadingData.getStatus().getValue());
            st.setInt(2, newBookReadingData.getStartYear());
            st.setInt(3, newBookReadingData.getStartMonth());
            st.setInt(4, newBookReadingData.getEndYear());
            st.setInt(5, newBookReadingData.getEndMonth());
            st.setBoolean(6, newBookReadingData.isReread());
            st.setInt(7, lastBookReading.getId());
            st.executeUpdate();
            st.close();
        }
    }

    public BookReading getBookReadingInfoForBook(Book book, Connection conn) throws SQLException
    {
        if (book.getStatus() == BookStatus.UNREAD) {
            return getDefaultBookReading();
        }

        String sql = "SELECT * FROM book_reading WHERE book_id=? AND status=? ORDER BY id DESC";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, book.getId());
        st.setInt(2, book.getStatus().getValue());
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return mapResultSetRowToBookReading(rs);
        }

        return getDefaultBookReading();
    }

    public BookReading getBookReadingInfoForBook(Book book) throws SQLException
    {
        if (book.getStatus() == BookStatus.UNREAD) {
            return getDefaultBookReading();
        }
        try (Connection conn = ds.getConnection()) {
            return getBookReadingInfoForBook(book, conn);
        }
    }

    public PageResult<BookReadingHistoryItem> getListForHistory(int userId, int year, PageRequest pageRequest) throws SQLException
    {
        try (Connection conn = ds.getConnection()) {
            QueryBuilder queryBuilder = new QueryBuilder();
            queryBuilder
                .table("book as b INNER JOIN book_reading as br ON (br.book_id=b.id)")
                .andWhere("b.user_id=?", userId);
            if (year > 0) {
                queryBuilder.andWhere("GREATEST(br.start_year, br.end_year) = ?", year);
            }

            //get total count
            PreparedStatement st = queryBuilder.selectCount().createPreparedStatement(conn);
            ResultSet rs = st.executeQuery();
            int totalCount;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            } else {
                throw new SQLException("Cannot get total count");
            }
            st.close();
            rs.close();

            //get records
            st = queryBuilder
                .selectColumns("b.author, b.title, br.status, br.start_year, br.start_month, br.end_year, br.end_month, br.is_reread")
                .orderBy("GREATEST(br.start_year, br.end_year) DESC, start_month DESC")
                .limit(pageRequest.getPageSize())
                .offset(pageRequest.getOffset())
                .createPreparedStatement(conn);
            rs = st.executeQuery();
            List<BookReadingHistoryItem> result = new ArrayList<>();
            while (rs.next()) {
                BookReadingHistoryItem item = new BookReadingHistoryItem();
                item.setAuthor(rs.getString("author"));
                item.setTitle(rs.getString("title"));
                item.setStatus(BookStatus.getByValue(rs.getInt("status")));
                item.setReread(rs.getBoolean("is_reread"));

                int startYear = rs.getInt("start_year");
                item.setStart(valueHelper.getStringFromYearAndMonth(
                        startYear, rs.getInt("start_month")
                ));

                int endYear = rs.getInt("end_year");
                item.setEnd(valueHelper.getStringFromYearAndMonth(
                        endYear, rs.getInt("end_month")
                ));

                item.setYear(endYear > startYear ? endYear : startYear);

                result.add(item);
            }

            return new PageResult<>(result, totalCount, pageRequest);
        }
    }

    private BookReading mapResultSetRowToBookReading(ResultSet rs) throws SQLException
    {
        BookReading bookReading = new BookReading();
        bookReading.setId(rs.getInt("id"));
        bookReading.setStatus(BookStatus.getByValue(rs.getInt("status")));
        bookReading.setStartYear(rs.getInt("start_year"));
        bookReading.setStartMonth(rs.getInt("start_month"));
        bookReading.setEndYear(rs.getInt("end_year"));
        bookReading.setEndMonth(rs.getInt("end_month"));
        bookReading.setReread(rs.getBoolean("is_reread"));
        return bookReading;
    }

    private BookReading getDefaultBookReading()
    {
        BookReading bookReading = new BookReading();
        bookReading.setStatus(BookStatus.UNREAD);
        return bookReading;
    }
}
