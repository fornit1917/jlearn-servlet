package jlearn.servlet.service;

/**
 * Created by Vit on 20.10.2016.
 */
public class UserService
{
    public CommandResult<Integer> register()
    {
        return CommandResult.createOkResult();
    }

    public boolean authenticate(String identity, String password)
    {
        return false;
    }


}
