package model;

import java.io.Serializable;

public class UserGroups extends GenericModel implements Serializable {
    private String groupName;
    private String login;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
