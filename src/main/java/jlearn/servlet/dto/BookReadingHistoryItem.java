package jlearn.servlet.dto;

import java.util.HashMap;
import java.util.Map;

public class BookReadingHistoryItem
{
    private String author;
    private String title;
    private String start;
    private String end;
    private BookStatus status;
    private int year;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Map<String, Object> toMap()
    {
        Map<String, Object> result = new HashMap<>();
        result.put("author", author);
        result.put("title", title);
        result.put("start", start);
        result.put("end", end);
        result.put("year", year);
        result.put("status", status.getValue());
        return result;
    }
}
