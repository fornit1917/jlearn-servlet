package jlearn.servlet.service;


public class ServiceContainer
{
    private UserService userService;

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
