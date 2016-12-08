package jlearn.servlet.entity;

public class Book
{
    private int id;
    private String title = "";
    private String author = "";
    private boolean isFiction = true;
    private BookStatus status = BookStatus.UNREAD;

    public Book()
    {

    }

    public Book(Book src)
    {
        this.id = src.getId();
        this.title = src.getTitle();
        this.author = src.getAuthor();
        this.isFiction = src.isFiction();
        this.status = src.getStatus();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isFiction() {
        return isFiction;
    }

    public void setFiction(boolean fiction) {
        isFiction = fiction;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
