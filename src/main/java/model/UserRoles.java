package model;

import java.io.Serializable;

public class UserRoles extends GenericModel implements Serializable {
    private String login;
    private String roleName;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
