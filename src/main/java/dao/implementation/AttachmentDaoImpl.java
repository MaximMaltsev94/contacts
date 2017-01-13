package dao.implementation;

import dao.interfaces.AttachmentDao;
import exceptions.DaoException;
import model.Attachment;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxim on 27.09.2016.
 */
public class AttachmentDaoImpl implements AttachmentDao {
    private final static Logger LOG = LoggerFactory.getLogger(AttachmentDaoImpl.class);
    private Connection connection;

    public AttachmentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private Attachment parseResultSet(ResultSet rs) throws SQLException {
        Attachment attachment = new Attachment();
        attachment.setId(rs.getInt("id"));
        attachment.setFileName(rs.getString("file_name"));
        attachment.setFilePath(rs.getString("file_path"));
        attachment.setContactID(rs.getInt("id_contact"));
        attachment.setUploadDate(rs.getTimestamp("upload_date"));
        attachment.setComment(rs.getString("comment"));
        return attachment;
    }


    @Override
    public void insert(Attachment attachment) throws DaoException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `attachment` (`file_name`, `file_path`, `id_contact`, `upload_date`, `comment`) VALUES(?, ?, ?, ?, ?)")) {
            preparedStatement.setObject(1, attachment.getFileName());
            preparedStatement.setObject(2, attachment.getFilePath());
            preparedStatement.setObject(3, attachment.getContactID());

            String uploadDate = DateFormatUtils.format(attachment.getUploadDate(), "yyyy-MM-dd HH:mm:ss");
            preparedStatement.setObject(4, uploadDate);
            preparedStatement.setObject(5, attachment.getComment());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't insert attachment - {}", attachment, e);
            throw new DaoException("error while inserting attachment", e);
        }
    }

    @Override
    public void delete(Attachment attachment) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM attachment` WHERE `id` = ?")) {
            preparedStatement.setObject(1, attachment.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't delete attachment - {}", attachment, e);
            throw new DaoException("error while deleting attachment", e);
        }
    }


    @Override
    public void update(Attachment attachment) throws DaoException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `attachment` SET `file_name` = ?, `file_path` = ?,`id_contact` = ?,`upload_date` = ?,`comment` = ? WHERE `id` = ?")) {
            preparedStatement.setObject(1, attachment.getFileName());
            preparedStatement.setObject(2, attachment.getFilePath());
            preparedStatement.setObject(3, attachment.getContactID());

            String uploadDate = DateFormatUtils.format(attachment.getUploadDate(), "yyyy-MM-dd HH:mm:ss");
            preparedStatement.setObject(4, uploadDate);
            preparedStatement.setObject(5, attachment.getComment());
            preparedStatement.setObject(6, attachment.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't update attachment - {}", attachment, e);
            throw new DaoException("error while updating attachment", e);
        }
    }


    private PreparedStatement createGetByContactIdStatement(int contactId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `attachment` WHERE `id_contact` = ?");
        preparedStatement.setObject(1, contactId);
        return preparedStatement;
    }

    @Override
    public List<Attachment> getByContactId(int contactId) throws DaoException {
        List<Attachment> attachmentList = new ArrayList<>();
        try(PreparedStatement preparedStatement = createGetByContactIdStatement(contactId);
                ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Attachment attachment = parseResultSet(rs);
                attachmentList.add(attachment);
            }

        } catch (SQLException e) {
            LOG.error("can't get attachments list by contact id - {}", contactId, e);
            throw new DaoException("error while getting attachments by id", e);
        }
        return attachmentList;
    }

    private PreparedStatement createFindByFilePathStatement(String filePath) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `attachment` WHERE `file_path` like ?");
        preparedStatement.setString(1, "%" + filePath + "%");
        return preparedStatement;
    }

    @Override
    public Attachment getByFilePath(String filePath) throws DaoException {
        Attachment attachment = new Attachment();
        try(PreparedStatement preparedStatement = createFindByFilePathStatement(filePath);
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                attachment = parseResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.error("can't find attachment by file path - {}", filePath, e);
            throw new DaoException("error while getting attachment by file path", e);
        }
        return attachment;
    }
}