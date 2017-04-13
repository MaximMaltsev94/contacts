package dao.implementation;

import dao.interfaces.ContactGroupsDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.ContactGroups;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DaoUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactGroupsDaoImpl implements ContactGroupsDao {
    private static final Logger LOG = LoggerFactory.getLogger(ContactGroupsDaoImpl.class);
    private final String TABLE_NAME = "`contact_groups`";
    private ResultSetMapper<ContactGroups> rsMapper;
    private JdbcTemplate<ContactGroups> jdbcTemplate;

    public ContactGroupsDaoImpl(Connection connection) {
        this.jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            ContactGroups contactGroups = new ContactGroups();
            contactGroups.setGroupId(rs.getInt("id_group"));
            contactGroups.setContactID(rs.getInt("id_contact"));
            return contactGroups;
        };
    }

    @Override
    public List<ContactGroups> getByGroupId(int groupId) throws DaoException {
        LOG.info("selecting contact groups by group id - {}", groupId);
        String sql = String.format("SELECT * FROM %s WHERE `id_group` = ?", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql, groupId);
    }

    @Override
    public List<ContactGroups> getByContactId(int contactId) throws DaoException {
        LOG.info("selecting contact groups by contact id - {}", contactId);
        String sql = String.format("SELECT * FROM %s WHERE `id_contact` = ?", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql, contactId);
    }

    @Override
    public List<ContactGroups> getByContactIdIn(List<Integer> contactIdList) throws DaoException {
        LOG.info("selecting contact groups by contact id list - {}", contactIdList);
        if(contactIdList.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = String.format("SELECT * from %s WHERE `id_contact` %s", TABLE_NAME, DaoUtils.generateSqlInPart(contactIdList.size()));
        return jdbcTemplate.queryForList(rsMapper, sql, contactIdList.toArray());
    }

    @Override
    public void insert(List<ContactGroups> contactGroupsList) throws DaoException {
        LOG.info("inserting contact group list - {}", contactGroupsList);
        if(contactGroupsList.isEmpty())
            return;

        String sql = String.format("INSERT INTO %s (`id_group`, `id_contact`) VALUES(?, ?)", TABLE_NAME);
        List<Object[]> args = new ArrayList<>();
        for (ContactGroups contactGroups : contactGroupsList) {
            args.add(new Object[]{contactGroups.getGroupId(), contactGroups.getContactID()});
        }
        jdbcTemplate.batchUpdate(sql, args);
    }

    @Override
    public void deleteByContactId(int contactId) throws DaoException {
        LOG.info("deleting contact groups by contact id - {}", contactId);
        String sql = String.format("DELETE FROM %s WHERE `id_contact` = ?", TABLE_NAME);
        jdbcTemplate.update(sql, contactId);
    }
}
