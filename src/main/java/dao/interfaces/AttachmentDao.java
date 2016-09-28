package dao.interfaces;

import model.Attachment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface AttachmentDao {
    void insert(Connection connection, Attachment attachment) throws SQLException;
    void delete(Connection connection, Attachment attachment) throws SQLException;
    List<Attachment> getByContactId(int contactId);
}
