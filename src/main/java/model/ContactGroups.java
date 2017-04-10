package model;

import java.io.Serializable;

public class ContactGroups extends GenericModel implements Serializable {
    private int contactID;
    private String groupName;

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
