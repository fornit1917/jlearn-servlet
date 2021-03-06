package jlearn.servlet.service;

import jlearn.servlet.dto.BookSearchCriteria;
import jlearn.servlet.dto.Book;
import jlearn.servlet.dto.BookReading;
import jlearn.servlet.dto.BookStatus;
import jlearn.servlet.exception.NotFoundException;
import jlearn.servlet.service.utility.CommandResult;
import jlearn.servlet.service.utility.PageRequest;
import jlearn.servlet.service.utility.PageResult;
import jlearn.servlet.service.utility.QueryBuilder;
import jlearn.servlet.service.utility.ErrorDescriptor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookService
{
    private DataSource ds;
    private BookReadingService bookReadingService;

    public BookService(DataSource ds)
    {
        this.ds = ds;
    }

    public void setBookReadingService(BookReadingService bookReadingService)
    {
        this.bookReadingService = bookReadingService;
    }

    public PageResult<Book> getAll(BookSearchCriteria criteria, PageRequest pageRequest) throws SQLException
    {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.table("book").andWhere("user_id=?", criteria.getUserId());

        if (criteria.getStatus() == BookSearchCriteria.STATUS_READ) {
            queryBuilder.andWhere("status NOT IN (?, ?)", BookStatus.UNREAD.getValue(), BookStatus.ABORTED.getValue());
        } else if (criteria.getStatus() == BookSearchCriteria.STATUS_UNREAD) {
            queryBuilder.andWhere("status IN (?, ?)", BookStatus.UNREAD.getValue(), BookStatus.ABORTED.getValue());
        }

        if (criteria.getType() == BookSearchCriteria.TYPE_FICTION) {
            queryBuilder.andWhere("is_fiction = ?", true);
        } else if (criteria.getType() == BookSearchCriteria.TYPE_NOT_FICTION) {
            queryBuilder.andWhere("is_fiction = ?", false);
        }

        if (!criteria.getTitle().isEmpty()) {
            queryBuilder.andWhere("LOWER(title) LIKE LOWER(?)", "%" + criteria.getTitle() + "%");
        }
        if (!criteria.getAuthor().isEmpty()) {
            queryBuilder.andWhere("LOWER(author) LIKE LOWER(?)", "%" + criteria.getAuthor() + "%");
        }

        try (Connection conn = ds.getConnection()) {
            //get total count
            PreparedStatement st = queryBuilder.selectCount().createPreparedStatement(conn);
            ResultSet rs = st.executeQuery();
            rs.next();
            int totalCount = rs.getInt(1);
            rs.close();

            st = queryBuilder
                    .selectColumns("*")
                    .orderBy("id desc")
                    .limit(pageRequest.getPageSize())
                    .offset(pageRequest.getOffset())
                    .createPreparedStatement(conn);
            rs = st.executeQuery();
            List<Book> books = new ArrayList<>(pageRequest.getPageSize());
            while (rs.next()) {
                Book book = mapResultSetRowToBook(rs);
                books.add(book);
            }

            return new PageResult<>(books, totalCount, pageRequest);
        }
    }

    public CommandResult<Book> addBook(int userId, Book book, BookReading bookReading) throws SQLException
    {
        ErrorDescriptor validateError = validateBook(book);
        if (validateError != null) {
            return CommandResult.createErrorResult(validateError);
        }

        if (book.getStatus() != BookStatus.UNREAD) {
            validateError = bookReadingService.validateBookReading(bookReading);
            if (validateError != null) {
                return CommandResult.createErrorResult(validateError);
            }
        }

        try (Connection conn = ds.getConnection()) {
            try {
                conn.setAutoCommit(false);
                int bookId = insertBook(userId, book, conn);
                book.setId(bookId);
                if (book.getStatus() != BookStatus.UNREAD) {
                    bookReadingService.addBookReadingForBook(book, bookReading, conn);
                }
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                book.setId(0);
                throw e;
            }

            conn.commit();
            conn.setAutoCommit(true);
        }

        return CommandResult.createOkResult(book);
    }

    public CommandResult<Book> editBook(int bookId, int userId, Book newBookData, BookReading newBookReadingData) throws SQLException, NotFoundException
    {
        ErrorDescriptor validateError = validateBook(newBookData);
        if (validateError != null) {
            return CommandResult.createErrorResult(validateError);
        }

        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Book book = getBookByIdAndUser(bookId, userId);
                if (book == null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    throw new NotFoundException();
                }

                if (!Arrays.asList(getAllowedNextStatuses(book.getStatus())).contains(newBookData.getStatus())) {
                    return CommandResult.createErrorResult("Incorrect status");
                }

                newBookData.setId(bookId);
                updateBook(book, newBookData, conn);
                if (newBookData.getStatus() != BookStatus.UNREAD) {
                    if (book.getStatus() == BookStatus.IN_PROGRESS || book.getStatus() == newBookData.getStatus()) {
                        bookReadingService.updateLastBookReadingForBook(book, newBookReadingData, conn);
                    } else {
                        bookReadingService.addBookReadingForBook(newBookData, newBookReadingData, conn);
                    }
                }
                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw e;
            }
        }
        return CommandResult.createOkResult(newBookData);
    }

    public Book getBookByIdAndUser(int bookId, int userId) throws SQLException
    {
        String sql = "SELECT * FROM book WHERE id=? AND user_id=?";
        try (Connection conn = ds.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, bookId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return mapResultSetRowToBook(rs);
        }
    }

    public void deleteBook(int userId, int bookId) throws SQLException
    {
        try (Connection conn = ds.getConnection()) {
            String sql = "DELETE from book WHERE user_id=? AND id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, userId);
            st.setInt(2, bookId);
            st.executeUpdate();
        }
    }

    public BookStatus[] getAllowedNextStatuses(BookStatus status)
    {
        switch (status) {
            case UNREAD:
                return BookStatus.values();
            case IN_PROGRESS:
                return new BookStatus[]{
                    BookStatus.IN_PROGRESS,
                    BookStatus.FINISHED,
                    BookStatus.ABORTED,
                };
            case FINISHED:
                return new BookStatus[]{
                    BookStatus.FINISHED,
                    BookStatus.IN_PROGRESS,
                };
            case ABORTED:
                return new BookStatus[]{
                    BookStatus.ABORTED,
                    BookStatus.IN_PROGRESS,
                };
            default:
                return new BookStatus[]{ status };
        }
    }

    public BookReading getBookReadingInfo(Book book) throws SQLException
    {
        return bookReadingService.getBookReadingInfoForBook(book);
    }

    private ErrorDescriptor validateBook(Book book)
    {
        if (book.getAuthor().isEmpty()) {
            return new ErrorDescriptor("Author is required");
        }
        if (book.getTitle().isEmpty()) {
            return new ErrorDescriptor("Title is required");
        }
        return null;
    }

    private int insertBook(int userId, Book book, Connection conn) throws SQLException
    {
        String sql = "INSERT INTO book (user_id, author, title, is_fiction, status) VALUES (?,?,?,?,?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, userId);
            st.setString(2, book.getAuthor());
            st.setString(3, book.getTitle());
            st.setBoolean(4, book.isFiction());
            st.setInt(5, book.getStatus().getValue());
            st.executeUpdate();
            if (!st.getGeneratedKeys().next()) {
                throw new SQLException("Cannot insert new book");
            }
            return st.getGeneratedKeys().getInt(1);
        }
    }

    private void updateBook(Book book, Book newBookData, Connection conn) throws SQLException
    {
        String sql = "UPDATE book SET author=?, title=?, is_fiction=?, status=? WHERE id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, newBookData.getAuthor());
            st.setString(2, newBookData.getTitle());
            st.setBoolean(3, newBookData.isFiction());
            st.setInt(4, newBookData.getStatus().getValue());
            st.setInt(5, book.getId());
            st.executeUpdate();
        }
    }

    private Book mapResultSetRowToBook(ResultSet rs) throws SQLException
    {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setAuthor(rs.getString("author"));
        book.setTitle(rs.getString("title"));
        book.setStatus(BookStatus.getByValue(rs.getInt("status")));
        book.setFiction(rs.getBoolean("is_fiction"));
        return book;
    }
}
