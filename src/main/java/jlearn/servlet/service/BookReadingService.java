package jlearn.servlet.service;

import jlearn.servlet.entity.Book;
import jlearn.servlet.entity.BookReading;
import jlearn.servlet.entity.BookStatus;
import jlearn.servlet.service.utility.CommandResult;
import jlearn.servlet.service.utility.ErrorDescriptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookReadingService
{
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
    }
}
