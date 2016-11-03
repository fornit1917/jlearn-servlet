package jlearn.servlet.service;


public class ServiceContainer
{
    private UserService userService;
    private InviteService inviteService;

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
}
