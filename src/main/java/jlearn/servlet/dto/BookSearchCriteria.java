package jlearn.servlet.dto;

public class BookSearchCriteria
{
    public static final int STATUS_ALL = 1;
    public static final int STATUS_UNREAD = 2;
    public static final int STATUS_READ = 3;

    public static final int TYPE_ALL = 1;
    public static final int TYPE_FICTION = 2;
    public static final int TYPE_NOT_FICTION = 3;

    private int userId;
    private String title = "";
    private String author = "";
    private int status = STATUS_ALL;
    private int type = TYPE_ALL;

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        if (title == null) {
            title = "";
        }
        this.title = title.trim();
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        if (author == null) {
            author = "";
        }
        this.author = author;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
