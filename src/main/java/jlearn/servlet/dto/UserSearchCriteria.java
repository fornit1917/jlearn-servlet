package jlearn.servlet.dto;

public class UserSearchCriteria
{
    public static final int STATE_ALL = 1;
    public static final int STATE_ACTIVE = 2;
    public static final int STATE_INACTIVE = 3;

    private String email;
    private int state;
    private boolean isOnlyPublic;

    public UserSearchCriteria(String email)
    {
        this.email = email == null ? "" : email;
    }

    public UserSearchCriteria(String email, int state)
    {
        this.email = email == null ? "" : email;
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? "" : email;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isOnlyPublic() {
        return isOnlyPublic;
    }

    public void setOnlyPublic(boolean onlyPublic) {
        isOnlyPublic = onlyPublic;
    }
}