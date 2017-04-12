package model;

import java.io.Serializable;

public class ContactGroups extends GenericModel implements Serializable {
    private int contactID;
    private int groupId;

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public ContactGroups() {
    }

    public ContactGroups(int groupId, int contactID) {
        this.contactID = contactID;
        this.groupId = groupId;
    }
}
