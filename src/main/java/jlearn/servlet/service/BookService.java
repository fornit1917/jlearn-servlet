package jlearn.servlet.service;

import jlearn.servlet.entity.Book;
import jlearn.servlet.service.utility.CommandResult;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class BookService
{
    private DataSource ds;

    public BookService(DataSource ds)
    {
        this.ds = ds;
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
