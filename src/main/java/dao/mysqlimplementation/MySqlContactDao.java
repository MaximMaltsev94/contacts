package dao.mysqlimplementation;

import dao.interfaces.ConnectionFactory;
import dao.interfaces.ContactDao;
import model.Contact;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlContactDao implements ContactDao {
    private final static Logger LOG = LoggerFactory.getLogger(MySqlContactDao.class);
    private ConnectionFactory connectionFactory;

    private Contact parseResultSet(ResultSet rs) throws SQLException {
        Contact contact = new Contact();

        contact.setId(rs.getInt("id"));
        contact.setFirstName(rs.getString("first_name" ));
        contact.setLastName(rs.getString("last_name"));
        contact.setPatronymic(rs.getString("patronymic"));
        contact.setBirthDate(rs.getDate("birth_date"));
        contact.setGender(rs.getBoolean("gender"));
        contact.setCitizenship(rs.getString("citizenship"));
        contact.setRelationshipID(rs.getByte("id_relationship"));
        contact.setWebSite(rs.getString("web_site"));
        contact.setEmail(rs.getString("email"));
        contact.setCompanyName(rs.getString("company_name"));
        contact.setProfilePicture(rs.getString("profile_picture"));
        contact.setCountryID(rs.getByte("id_country"));
        contact.setCityID(rs.getByte("id_city"));
        contact.setStreet(rs.getString("street"));
        contact.setPostcode(rs.getString("postcode"));

        return contact;
    }

    private void fillPreparedStatement(PreparedStatement preparedStatement, Contact contact) throws SQLException {
        preparedStatement.setObject(1, contact.getFirstName());
        preparedStatement.setObject(2, contact.getLastName());
        preparedStatement.setObject(3, contact.getPatronymic());

        String birthDate = null;
        if(contact.getBirthDate() != null) {
            birthDate = DateFormatUtils.format(contact.getBirthDate(), "yyyy.MM.dd");
        }

        preparedStatement.setObject(4, birthDate);
        preparedStatement.setObject(5, contact.getGender());
        preparedStatement.setObject(6, contact.getCitizenship());
        preparedStatement.setObject(7, contact.getRelationshipID());
        preparedStatement.setObject(8, contact.getWebSite());
        preparedStatement.setObject(9, contact.getEmail());
        preparedStatement.setObject(10, contact.getCompanyName());
        preparedStatement.setObject(11, contact.getProfilePicture());
        preparedStatement.setObject(12, contact.getCountryID() == 0 ? null : contact.getCountryID());
        preparedStatement.setObject(13, contact.getCityID() == 0 ? null : contact.getCityID());
        preparedStatement.setObject(14, contact.getStreet());
        preparedStatement.setObject(15, contact.getPostcode());

    }

    public MySqlContactDao() throws NamingException {
        connectionFactory = MySqlConnectionFactory.getInstance();
    }

    @Override
    public void insert(Contact contact) {
        try(Connection con = connectionFactory.getConnection();
            PreparedStatement pStatement = con.prepareStatement("INSERT INTO `contacts_maltsev`.`contact` (`first_name`, `last_name`, `patronymic`, `birth_date`, `gender`, `citizenship`, `id_relationship`, `web_site`, `email`, `company_name`, `profile_picture`, `id_country`, `id_city`, `street`, `postcode`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            fillPreparedStatement(pStatement, contact);
            pStatement.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.warn("can't insert contact", e);
        }

    }

    @Override
    public void update(Contact contact) {
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `contacts_maltsev`.`contact` SET `first_name` = ?, `last_name` = ?, `patronymic` = ?, `birth_date` = ?, `gender` = ?,`citizenship` = ?, `id_relationship` = ?, `web_site` = ?, `email` = ?, `company_name` = ?, `profile_picture` = ?, `id_country` = ?, `id_city` = ?, `street` = ?, `postcode` = ? WHERE `id` = ?");){
            fillPreparedStatement(preparedStatement, contact);
            preparedStatement.setObject(16, contact.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOG.warn("can't update contact id - {}", contact.getId(), e);
        }

    }

    @Override
    public void delete(Contact contact) {

    }

    @Override
    public void deleteByID(int id) {
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement pStatement = connection.prepareStatement("DELETE FROM `contacts_maltsev`.`contact` WHERE id = ?")) {
            pStatement.setObject(1, id);
            pStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            LOG.warn("can't delete contact by id - {}", id, e);
        }
    }

    private PreparedStatement createGetByIDStatement(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`contact` WHERE id = ?");
        preparedStatement.setObject(1, id);
        return preparedStatement;
    }
    @Override
    public Contact getByID(int id) {
        Contact contact = null;
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = createGetByIDStatement(connection, id);
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                contact = parseResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.warn("can't get contact by id - {}", id, e);
        }
        return contact;

    }

    @Override
    public int getMaxID() {
        int maxID = 0;
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(`id`) AS mx FROM `contacts_maltsev`.`contact`");
            ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                maxID = rs.getInt("mx");
            }
        } catch (SQLException e) {
            LOG.warn("can't get max ID ", e);
        }
        return maxID;
    }


    private PreparedStatement createGetContactsPageStatement(Connection connection, int pageNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`contact` LIMIT ?, 20");
        preparedStatement.setObject(1, (pageNumber - 1) * 20);
        return preparedStatement;
    }
    @Override
    public List<Contact> getContactsPage(int pageNumber) {
        List<Contact> contactList = new ArrayList<>();
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = createGetContactsPageStatement(connection, pageNumber);
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Contact contact = parseResultSet(rs);
                contactList.add(contact);
            }
        } catch (SQLException e) {
            LOG.warn("can't get contacts list", e);
        }
        return contactList;
    }

    @Override
    public int getRowsCount() {
        int count = 0;
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(`id`) AS `cnt` FROM `contact`");
            ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (SQLException e) {
            LOG.warn("can't get contacts count", e);
        }
        return count;
    }
}
