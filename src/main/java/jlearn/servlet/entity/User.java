package jlearn.servlet.entity;

/**
 * Created by Vit on 02.11.2016.
 */
public class User
{
    private int id;
    private String email;
    private String authKey;
    private String hpassw;
    private boolean isAdmin;
    private boolean isActive;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getHpassw() {
        return hpassw;
    }

    public void setHpassw(String hpassw) {
        this.hpassw = hpassw;
    }
}
