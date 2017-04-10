package model;

import java.io.Serializable;

public class UserGroups extends GenericModel implements Serializable {
    private int id;
    private String groupName;
    private String login;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public UserGroups() {
    }

    public UserGroups(int id, String groupName, String login) {
        this.id = id;
        this.groupName = groupName;
        this.login = login;
    }

    public UserGroups(String groupName, String login) {
        this.groupName = groupName;
        this.login = login;
    }
}
