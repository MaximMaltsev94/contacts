package dao.interfaces;

import exceptions.DaoException;
import model.Attachment;

import java.util.List;

public interface AttachmentDao {
    void insert(Attachment attachment) throws DaoException;
    void insert(List<Attachment> attachmentList) throws DaoException;

    void delete(Attachment attachment) throws DaoException;
    void delete(List<Attachment> attachmentList) throws DaoException;

    void update(Attachment attachment) throws DaoException;
    void update(List<Attachment> attachmentList) throws DaoException;

    List<Attachment> getByContactId(int contactId) throws DaoException;
    List<Attachment> getByContactIdIn(List<Integer> contactIdList) throws DaoException;
    Attachment getByFilePath(String filePath) throws DaoException;
}
