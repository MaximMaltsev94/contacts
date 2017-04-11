package dao.implementation;

import dao.interfaces.ContactGroupsDao;
import exceptions.DaoException;
import model.ContactGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactGroupsDaoImpl implements ContactGroupsDao {
    private static final Logger LOG = LoggerFactory.getLogger(ContactGroupsDaoImpl.class);
    private Connection connection;

    public ContactGroupsDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private ContactGroups parseResultSet(ResultSet rs) throws SQLException {
        ContactGroups contactGroups = new ContactGroups();
        contactGroups.setGroupId(rs.getInt("id_group"));
        contactGroups.setContactID(rs.getInt("id_contact"));
        return contactGroups;
    }

    private PreparedStatement createGetByGroupIdStatement(int groupId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `contact_groups` WHERE `id_group` = ?");
        statement.setObject(1, groupId);
        return statement;
    }

    @Override
    public List<ContactGroups> getByGroupId(int groupId) throws DaoException {
        List<ContactGroups> result = new ArrayList<>();
        try(PreparedStatement statement = createGetByGroupIdStatement(groupId);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result.add(parseResultSet(rs));
            }
        } catch (SQLException e) {
            LOG.error("can't get contact groups by group id - {}", groupId, e);
            throw new DaoException("error while getting contact groups by contact id", e);
        }
        return result;
    }
}
