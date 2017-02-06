package service;

import dao.implementation.AttachmentDaoImpl;
import dao.interfaces.AttachmentDao;
import exceptions.DaoException;
import exceptions.RequestParseException;
import model.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactFileUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class AttachmentServiceImpl implements AttachmentService {
    private final static Logger LOG = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private AttachmentDao attachmentDao;

    public AttachmentServiceImpl(Connection connection) {
        attachmentDao = new AttachmentDaoImpl(connection);
    }

    private String writeFileToFileSystem(InputStream inputStream, String fileExtension) {
        try {
            File file = ContactFileUtils.createTempFile("file", fileExtension);
            FileUtils.copyInputStreamToFile(inputStream, file);
            return file.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Attachment> parseRequest(HttpServletRequest request, int contactId) throws RequestParseException {
        List<Attachment> attachmentList = new ArrayList<>();
        try {
            Enumeration<String> attributeNames = request.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attrName = attributeNames.nextElement();
                if(!StringUtils.contains(attrName, "name_attachment-")) {
                    continue;
                }

                int i = Integer.parseInt(StringUtils.substringAfter(attrName, "name_attachment-"));

                Attachment attachment = new Attachment();
                attachment.setContactID(contactId);
                attachment.setFileName((String) request.getAttribute("name_attachment-" + i));

                Object idAttachment = request.getAttribute("id_attachment-" + i);
                if(idAttachment != null) {
                    attachment.setId(Integer.parseInt((String) idAttachment ));
                }
                attachment.setFilePath((String) request.getAttribute("path_attachment-" + i));

                String uploadDate = (String) request.getAttribute("date_attachment-" + i);
                if (StringUtils.isNotBlank(uploadDate)) {
                    attachment.setUploadDate(DateUtils.parseDate(uploadDate, "yyyy-MM-dd HH:mm:ss.S"));
                }

                attachment.setComment((String) request.getAttribute("comment_attachment-" + i));

                InputStream is = (InputStream) request.getAttribute("file_attachment-" + i);
                if(is != null) {
                    String fileExtension = (String) request.getAttribute("file_attachment-" + i + ":fileExtension");
                    String filePath = writeFileToFileSystem(is, fileExtension);
                    attachment.setFilePath("?action=document&name=" + filePath);
                }

                attachmentList.add(attachment);
            }
        } catch (ParseException e) {
            LOG.error("error while parsing attachment upload date", e);
            throw new RequestParseException("attachment upload date doesn't match expected format yyyy-MM-dd HH:mm:ss.S", e);
        }
        return attachmentList;
    }

    @Override
    public void deleteAttachmentFile(Attachment attachment) {
        if(attachment.getFilePath() != null)
            ContactFileUtils.deleteFileByUrl(attachment.getFilePath(), "file");
    }

    @Override
    public void insert(Attachment attachment) throws DaoException {
        attachmentDao.insert(attachment);
    }

    @Override
    public void insert(List<Attachment> attachmentList) throws DaoException {
        attachmentDao.insert(attachmentList);
    }

    @Override
    public void delete(Attachment attachment, boolean deleteFile) throws DaoException {
        if (deleteFile) {
            deleteAttachmentFile(attachment);
        }

        attachmentDao.delete(attachment);
    }

    @Override
    public void delete(List<Attachment> attachmentList, boolean deleteFile) throws DaoException {
        if (deleteFile) {
            for (Attachment attachment : attachmentList) {
                deleteAttachmentFile(attachment);
            }
        }

        attachmentDao.delete(attachmentList);
    }

    @Override
    public void update(Attachment attachment) throws DaoException {
        attachmentDao.update(attachment);

    }

    @Override
    public void update(List<Attachment> attachmentList) throws DaoException {
        attachmentDao.update(attachmentList);
    }

    @Override
    public List<Attachment> getByContactId(int contactId) throws DaoException {
        return attachmentDao.getByContactId(contactId);
    }

    @Override
    public List<Attachment> getByContactIdIn(List<Integer> contactIdList) throws DaoException {
        return attachmentDao.getByContactIdIn(contactIdList);
    }

    @Override
    public Attachment getByFilePath(String filePath) throws DaoException {
        return attachmentDao.getByFilePath(filePath);
    }
}
