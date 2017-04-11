package dao.implementation;

import dao.interfaces.ContactGroupsDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.ContactGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
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
}
