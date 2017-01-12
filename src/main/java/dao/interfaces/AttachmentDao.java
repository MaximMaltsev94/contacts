package dao.interfaces;

import exceptions.DaoException;
import model.Attachment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface AttachmentDao {
    void insert(Attachment attachment) throws DaoException;
    void delete(Attachment attachment) throws DaoException;
    void update(Attachment attachment) throws DaoException;
    List<Attachment> getByContactId(int contactId) throws DaoException;
    Attachment getByFilePath(String filePath) throws DaoException;
}
