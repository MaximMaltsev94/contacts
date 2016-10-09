package dao.interfaces;

import model.Attachment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface AttachmentDao {
    void insert(Attachment attachment) throws SQLException;
    void delete(Attachment attachment) throws SQLException;
    void update(Attachment attachment);
    List<Attachment> getByContactId(int contactId);
    Attachment findByFilePath(String filePath);
}
