package jlearn.servlet.service;

import jlearn.servlet.dto.BookSearchCriteria;
import jlearn.servlet.entity.Book;
import jlearn.servlet.entity.BookStatus;
import jlearn.servlet.service.utility.CommandResult;
import jlearn.servlet.service.utility.PageRequest;
import jlearn.servlet.service.utility.PageResult;
import jlearn.servlet.service.utility.QueryBuilder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService
{
    private DataSource ds;

    public BookService(DataSource ds)
    {
        this.ds = ds;
    }

    public PageResult<Book> getAll(BookSearchCriteria criteria, PageRequest pageRequest) throws SQLException
    {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.table("book").andWhere("user_id=?", criteria.getUserId());

        try (Connection conn = ds.getConnection()) {
            //get total count
            PreparedStatement st = queryBuilder.selectCount().createPreparedStatement(conn);
            ResultSet rs = st.executeQuery();
            rs.next();
            int totalCount = rs.getInt(1);
            rs.close();

            st = queryBuilder
                    .selectColumns("*")
                    .limit(pageRequest.getPageSize())
                    .offset(pageRequest.getOffset())
                    .createPreparedStatement(conn);
            rs = st.executeQuery();
            List<Book> books = new ArrayList<>(pageRequest.getPageSize());
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setAuthor(rs.getString("author"));
                book.setTitle(rs.getString("title"));
                book.setStatus(BookStatus.getByValue(rs.getInt("status")));
                books.add(book);
            }

            return new PageResult<>(books, totalCount, pageRequest);
        }
    }

    public CommandResult<Book> addBook(int userId, Book book) throws SQLException
    {
        if (book.getAuthor().isEmpty()) {
            return CommandResult.createErrorResult("Author is required");
        }
        if (book.getTitle().isEmpty()) {
            return CommandResult.createErrorResult("Title is required");
        }

        try (Connection conn = ds.getConnection()) {
            int bookId = insertBook(userId, book, conn);
            book.setId(bookId);
        }

        return CommandResult.createOkResult(book);
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

    private void updateBook(Book book, Connection conn) throws SQLException
    {
        String sql = "UPDATE book SET author=?, title=?, is_fiction=?, status=? WHERE id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, book.getAuthor());
            st.setString(2, book.getTitle());
            st.setBoolean(3, book.isFiction());
            st.setInt(4, book.getStatus().getValue());
            st.setInt(5, book.getId());
        }
    }
}
