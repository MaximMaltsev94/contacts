package model;

import java.io.Serializable;
import java.util.Date;

public class Attachment implements Serializable {
    private int id;
    private String fileName;
    private String filePath;
    private String contactID;
    private Date uploadDate;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Attachment(int id, String fileName, String filePath, String contactID, Date uploadDate, String comment) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.contactID = contactID;
        this.uploadDate = uploadDate;
        this.comment = comment;
    }

    public Attachment() {
    }
}
