package jlearn.servlet.dto;

public class UserSearchCriteria
{
    public static final int STATE_ALL = 1;
    public static final int STATE_ACTIVE = 2;
    public static final int STATE_INACTIVE = 3;

    private String email;
    private int state;

    public UserSearchCriteria(String email, int state)
    {
        this.email = email == null ? "" : email;
        this.state = state;
    }

    public String getEmail()
    {
        return email;
    }

    public int getState()
    {
        return state;
    }
}