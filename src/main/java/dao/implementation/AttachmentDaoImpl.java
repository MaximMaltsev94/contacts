package dao.implementation;

import dao.interfaces.AttachmentDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.Attachment;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DaoUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AttachmentDaoImpl implements AttachmentDao {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentDaoImpl.class);
    private final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final String TABLE_NAME = "`attachment`";
    private ResultSetMapper<Attachment> rsMapper;
    private JdbcTemplate<Attachment> jdbcTemplate;

    public AttachmentDaoImpl(Connection connection) {
        this.jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            Attachment attachment = new Attachment();
            attachment.setId(rs.getInt("id"));
            attachment.setFileName(rs.getString("file_name"));
            attachment.setFilePath(rs.getString("file_path"));
            attachment.setContactID(rs.getInt("id_contact"));
            attachment.setUploadDate(rs.getTimestamp("upload_date"));
            attachment.setComment(rs.getString("comment"));
            return attachment;
        };
    }

    @Override
    public void insert(Attachment attachment) throws DaoException {
        LOG.info("inserting attachment - {}", attachment);
        String sql = String.format("INSERT INTO %s (`file_name`, `file_path`, `id_contact`, `upload_date`, `comment`) VALUES(?, ?, ?, ?, ?)", TABLE_NAME);
        jdbcTemplate.update(sql, attachment.getFileName(),
                                    attachment.getFilePath(),
                                    attachment.getContactID(),
                                    DateFormatUtils.format(attachment.getUploadDate(), DATE_FORMAT),
                                    attachment.getComment());
    }

    @Override
    public void insert(List<Attachment> attachmentList) throws DaoException {
        LOG.info("inserting attachment list - {}", attachmentList);
        if(attachmentList.isEmpty()) {
            return;
        }
        String sql = String.format("INSERT INTO %s (`file_name`, `file_path`, `id_contact`, `upload_date`, `comment`) VALUES(?, ?, ?, ?, ?)", TABLE_NAME);
        List<Object[]> params = new ArrayList<>();
        for (Attachment attachment : attachmentList) {
            params.add(new Object[]{attachment.getFileName(),
                                    attachment.getFilePath(),
                                    attachment.getContactID(),
                                    DateFormatUtils.format(attachment.getUploadDate(), DATE_FORMAT),
                                    attachment.getComment()});
        }
        jdbcTemplate.batchUpdate(sql, params);
    }

    @Override
    public void delete(Attachment attachment) throws DaoException {
        LOG.info("deleting attachment - {}", attachment);
        String sql = String.format("DELETE FROM %s WHERE `id` = ?", TABLE_NAME);
        jdbcTemplate.update(sql, attachment.getId());
    }

    @Override
    public void delete(List<Attachment> attachmentList) throws DaoException {
        LOG.info("deleting attachment list - {}", attachmentList);
        if(attachmentList.isEmpty())
            return;
        List<Integer> idList = attachmentList.stream().map(Attachment::getId).collect(Collectors.toList());
        String sql = String.format("DELETE FROM %s WHERE `id` %s ", TABLE_NAME, DaoUtils.generateSqlInPart(idList.size()));
        jdbcTemplate.update(sql, idList.toArray());
    }

    @Override
    public void update(Attachment attachment) throws DaoException {
        LOG.info("updating attachment - {}", attachment);
        String sql = String.format("UPDATE %s SET `file_name` = ?, `file_path` = ?,`id_contact` = ?,`upload_date` = ?,`comment` = ? WHERE `id` = ?", TABLE_NAME);
        jdbcTemplate.update(sql, attachment.getFileName(),
                                attachment.getFilePath(),
                                attachment.getContactID(),
                                DateFormatUtils.format(attachment.getUploadDate(), DATE_FORMAT),
                                attachment.getComment(),
                                attachment.getId());
    }

    @Override
    public void update(List<Attachment> attachmentList) throws DaoException {
        LOG.info("updating attachment list - {}", attachmentList);
        if(attachmentList.isEmpty())
            return;
        String sql = String.format("UPDATE %s SET `file_name` = ?, `file_path` = ?,`id_contact` = ?,`upload_date` = ?,`comment` = ? WHERE `id` = ?", TABLE_NAME);
        List<Object[]> params = new ArrayList<>();
        for (Attachment attachment : attachmentList) {
            params.add(new Object[]{attachment.getFileName(),
                                    attachment.getFilePath(),
                                    attachment.getContactID(),
                                    DateFormatUtils.format(attachment.getUploadDate(), DATE_FORMAT),
                                    attachment.getComment(),
                                    attachment.getId()});
        }
        jdbcTemplate.batchUpdate(sql, params);
    }

    @Override
    public List<Attachment> getByContactId(int contactId) throws DaoException {
        LOG.info("selecting attachments by contact id - {}", contactId);
        String sql = String.format("SELECT * FROM %s WHERE `id_contact` = ?", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql, contactId);
    }

    @Override
    public List<Attachment> getByContactIdIn(List<Integer> contactIdList) throws DaoException {
        LOG.info("selecting attachments by contact id list - {}", contactIdList);
        if(contactIdList.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = String.format("SELECT * FROM %s WHERE `id_contact` %s ", TABLE_NAME, DaoUtils.generateSqlInPart(contactIdList.size()));
        return jdbcTemplate.queryForList(rsMapper, sql, contactIdList.toArray());
    }

    @Override
    public Attachment getByFilePath(String filePath) throws DaoException {
        LOG.info("selecting attachments by file path - {}", filePath);
        String sql = String.format("SELECT * FROM %s WHERE `file_path` like ?", TABLE_NAME);
        return jdbcTemplate.queryForObject(rsMapper, sql,"%" + filePath + "%");
    }
}
