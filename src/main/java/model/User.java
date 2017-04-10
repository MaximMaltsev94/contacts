package model;

import java.io.Serializable;

public class User extends GenericModel implements Serializable {
    private String login;
    private String email;
    private boolean needBDateNotify;
    private String profilePicture;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public boolean getNeedBDateNotify() {
        return needBDateNotify;
    }

    public void setNeedBDateNotify(boolean needBDateNotify) {
        this.needBDateNotify = needBDateNotify;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
