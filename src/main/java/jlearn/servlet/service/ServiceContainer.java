package jlearn.servlet.service;


public class ServiceContainer
{
    private UserService userService;
    private InviteService inviteService;
    private BookService bookService;
    private BookReadingService bookReadingService;

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public InviteService getInviteService()
    {
        return inviteService;
    }

    public void setInviteService(InviteService inviteService)
    {
        this.inviteService = inviteService;
    }

    public BookService getBookService()
    {
        return bookService;
    }

    public void setBookService(BookService bookService)
    {
        this.bookService = bookService;
    }

    public BookReadingService getBookReadingService()
    {
        return bookReadingService;
    }

    public void setBookReadingService(BookReadingService bookReadingService)
    {
        this.bookReadingService = bookReadingService;
    }
}
