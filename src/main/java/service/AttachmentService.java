package service;

import exceptions.DaoException;
import exceptions.RequestParseException;
import model.Attachment;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AttachmentService {
    List<Attachment> parseRequest(HttpServletRequest request, int contactId) throws RequestParseException;
    void deleteAttachmentFile(Attachment attachment);


    void insert(Attachment attachment) throws DaoException;
    void insert(List<Attachment> attachmentList) throws DaoException;

    void delete(Attachment attachment, boolean deleteFile) throws DaoException;
    void delete(List<Attachment> attachmentList, boolean deleteFile) throws DaoException;

    void update(Attachment attachment) throws DaoException;
    void update(List<Attachment> attachmentList) throws DaoException;

    List<Attachment> getByContactId(int contactId) throws DaoException;
    Attachment getByFilePath(String filePath) throws DaoException;
}
