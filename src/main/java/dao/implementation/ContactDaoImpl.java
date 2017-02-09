package dao.implementation;

import dao.interfaces.ContactDao;
import exceptions.DaoException;
import model.Contact;
import model.ContactSearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl implements ContactDao {
    private final static Logger LOG = LoggerFactory.getLogger(ContactDaoImpl.class);
    private Connection connection;

    private Contact parseResultSet(ResultSet rs) throws SQLException {
        Contact contact = new Contact();

        contact.setId(rs.getInt("id"));
        contact.setFirstName(rs.getString("first_name"));
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
        if (contact.getBirthDate() != null) {
            birthDate = DateFormatUtils.format(contact.getBirthDate(), "yyyy.MM.dd");
        }

        preparedStatement.setObject(4, birthDate);
        preparedStatement.setObject(5, contact.getGender());
        preparedStatement.setObject(6, contact.getCitizenship());
        preparedStatement.setObject(7, contact.getRelationshipID() == 0 ? null : contact.getRelationshipID());
        preparedStatement.setObject(8, contact.getWebSite());
        preparedStatement.setObject(9, contact.getEmail());
        preparedStatement.setObject(10, contact.getCompanyName());
        preparedStatement.setObject(11, contact.getProfilePicture());
        preparedStatement.setObject(12, contact.getCountryID() == 0 ? null : contact.getCountryID());
        preparedStatement.setObject(13, contact.getCityID() == 0 ? null : contact.getCityID());
        preparedStatement.setObject(14, contact.getStreet());
        preparedStatement.setObject(15, contact.getPostcode());

    }

    public ContactDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Contact insert(Contact contact) throws DaoException {
        try (PreparedStatement pStatement = connection.prepareStatement("INSERT INTO `contact` (`first_name`, `last_name`, `patronymic`, `birth_date`, `gender`, `citizenship`, `id_relationship`, `web_site`, `email`, `company_name`, `profile_picture`, `id_country`, `id_city`, `street`, `postcode`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            fillPreparedStatement(pStatement, contact);
            pStatement.executeUpdate();

            try(ResultSet rs = pStatement.getGeneratedKeys()) {
                if(rs.next()) {
                    contact.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("can't insert contact - {}", contact, e);
            throw new DaoException("error while inserting contact", e);
        }
        return contact;
    }

    @Override
    public void update(Contact contact) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `contact` SET `first_name` = ?, `last_name` = ?, `patronymic` = ?, `birth_date` = ?, `gender` = ?,`citizenship` = ?, `id_relationship` = ?, `web_site` = ?, `email` = ?, `company_name` = ?, `profile_picture` = ?, `id_country` = ?, `id_city` = ?, `street` = ?, `postcode` = ? WHERE `id` = ?");) {
            fillPreparedStatement(preparedStatement, contact);
            preparedStatement.setObject(16, contact.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't update contact - {}", contact, e);
            throw new DaoException();
        }

    }

    @Override
    public void delete(Contact contact) throws DaoException {

    }

    @Override
    public void deleteByID(int id) throws DaoException {
        try (PreparedStatement pStatement = connection.prepareStatement("DELETE FROM `contact` WHERE id = ?")) {
            pStatement.setObject(1, id);
            pStatement.execute();
        } catch (SQLException e) {
            LOG.error("can't delete contact by id - {}", id, e);
            throw new DaoException("error while deleting contact by ID", e);
        }
    }

    private String createDeleteSql(int size) {
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM `contact` WHERE `id` in (");
        for(int i = 0; i < size; ++i) {
            sqlBuilder.append(" ?");
            if(i != size - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    @Override
    public void delete(List<Integer> idList) throws DaoException {
        if(idList.isEmpty())
            return;

        String sql = createDeleteSql(idList.size());
        try (PreparedStatement pStatement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= idList.size(); i++) {
                pStatement.setObject(i, idList.get(i - 1));
            }
            pStatement.execute();
        } catch (SQLException e) {
            LOG.error("can't delete contact by id list - {}", idList, e);
            throw new DaoException("error while deleting contacts by ID list", e);
        }
    }

    private PreparedStatement createGetByIDStatement(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contact` WHERE id = ?");
        preparedStatement.setObject(1, id);
        return preparedStatement;
    }

    @Override
    public Contact getByID(int id) throws DaoException {
        Contact contact = null;
        try (PreparedStatement preparedStatement = createGetByIDStatement(connection, id);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                contact = parseResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.error("can't get contact by id - {}", id, e);
            throw new DaoException("error while getting contact by ID", e);
        }
        return contact;
    }

    private String createGetByIdInSQL(int size) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM `contact` WHERE `id` in (");
        for(int i = 0; i < size; ++i) {
            sqlBuilder.append(" ?");
            if(i != size - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    private PreparedStatement createGetByIdInStatement(Connection connection, List<Integer> idList) throws SQLException {
        String sql = createGetByIdInSQL(idList.size());

        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 1; i <= idList.size(); i++) {
            statement.setObject(i, idList.get(i - 1));
        }
        return statement;
    }


    @Override
    public List<Contact> getByIdIn(List<Integer> idList) throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        if(idList.isEmpty()) {
            return contactList;
        }

        try(PreparedStatement statement = createGetByIdInStatement(connection, idList);
            ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Contact contact = parseResultSet(rs);
                contactList.add(contact);
            }

        } catch (SQLException e) {
            LOG.error("can't get contacts by id list - {}", idList, e);
            throw new DaoException("error while getting contacts by id list", e);
        }

        return contactList;
    }

    @Override
    public long getMaxID() throws DaoException {
        long maxID = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(`id`) AS mx FROM `contact`");
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                maxID = rs.getLong("mx");
            }
        } catch (SQLException e) {
            LOG.error("can't get max ID", e);
            throw new DaoException("error while getting maximum ID", e);
        }
        return maxID;
    }


    private PreparedStatement createGetContactsPageStatement(Connection connection, int pageNumber, int limit) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contact` ORDER BY `id` desc LIMIT ?, ?");
        preparedStatement.setObject(1, (pageNumber - 1) * limit);
        preparedStatement.setObject(2, limit);
        return preparedStatement;
    }

    @Override
    public List<Contact> get(int pageNumber, int limit) throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        try (PreparedStatement preparedStatement = createGetContactsPageStatement(connection, pageNumber, limit);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Contact contact = parseResultSet(rs);
                contactList.add(contact);
            }
        } catch (SQLException e) {
            LOG.error("can't get contacts list by page number - {}", pageNumber, e);
            throw new DaoException("error while getting contact page", e);
        }
        return contactList;
    }

    @Override
    public long getCount() throws DaoException {
        long count = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(`id`) AS `cnt` FROM `contact`");
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                count = rs.getLong("cnt");
            }
        } catch (SQLException e) {
            LOG.error("can't get contacts count", e);
            throw new DaoException("error while getting contacts count", e);
        }
        return count;
    }

    private String surroundPercentString(String val) {
        return StringUtils.isBlank(val) ? "%" : "%" + val.toLowerCase() + "%";
    }

    private String surroundPercentInt(int val, int template) {
        return val == template ? "%" : "%" + val + "%";
    }

    private PreparedStatement concatSearchQuery(Connection connection, ContactSearchCriteria searchCriteria, int pageNumber, int limit) throws SQLException {
        String sql = "SELECT * FROM `contact` WHERE " +
                "first_name like ? and " +
                "last_name like ? and " +
                "ifnull(patronymic, '') like ? and " +
                "ifnull(timestampdiff(YEAR, birth_date, current_date()), -1) between ? and ? and " +
                "cast((gender | 0) as char) like ? and " +
                "ifnull(citizenship, '') like ? and " +
                "ifnull(cast(id_relationship as char), '') like ? and " +
                "ifnull(company_name, '') like ? and " +
                "ifnull(cast(id_country as char), '') like ? and " +
                "ifnull(cast(id_city as char), '') like ? and " +
                "ifnull(street, '') like ? and " +
                "ifnull(postcode, '') like ? LIMIT ?, ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setObject(1, surroundPercentString(searchCriteria.getFirstName()));
        statement.setObject(2, surroundPercentString(searchCriteria.getLastName()));
        statement.setObject(3, surroundPercentString(searchCriteria.getPatronymic()));

        int age1 = searchCriteria.getAge1();
        int age2 = searchCriteria.getAge2();

        if(age1 == 0 && age2 == 0) {
            age2 = 200;
        }

        statement.setObject(4, age1);
        statement.setObject(5, age2);
        statement.setObject(6, surroundPercentInt(searchCriteria.getGender(), 2));
        statement.setObject(7, surroundPercentString(searchCriteria.getCitizenship()));
        statement.setObject(8, surroundPercentInt(searchCriteria.getRelationship(), 0));
        statement.setObject(9, surroundPercentString(searchCriteria.getCompanyName()));
        statement.setObject(10, surroundPercentInt(searchCriteria.getCountry(), 0));
        statement.setObject(11, surroundPercentInt(searchCriteria.getCity(), 0));
        statement.setObject(12, surroundPercentString(searchCriteria.getStreet()));
        statement.setObject(13, surroundPercentString(searchCriteria.getPostcode()));
        statement.setObject(14, (pageNumber - 1) * limit);
        statement.setObject(15, limit);

        LOG.info("search query: " + statement);
        return statement;
    }

    @Override
    public List<Contact> get(ContactSearchCriteria searchCriteria, int pageNumber, int limit) throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        try(PreparedStatement statement = concatSearchQuery(connection, searchCriteria, pageNumber, limit);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Contact contact = parseResultSet(rs);
                contactList.add(contact);
            }
        } catch (SQLException e) {
            LOG.error("can't perform search query", e);
            throw new DaoException("error while searching contacts by criteria", e);
        }
        return contactList;
    }

    @Override
    public List<Contact> getByEmailNotNull() throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contact` WHERE `email` IS NOT NULL");
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Contact contact = parseResultSet(rs);
                contactList.add(contact);
            }
        } catch (SQLException e) {
            LOG.error("can't get contacts with email", e);
            throw new DaoException("error while getting contacts with email", e);
        }
        return contactList;
    }

    @Override
    public List<Contact> getByBirthdayToday() throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from `contact` WHERE day(`birth_date`) = day(current_date()) and month(`birth_date`) = month(current_date())");
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Contact contact = parseResultSet(rs);
                contactList.add(contact);
            }
        } catch (SQLException e) {
            LOG.error("can't get contacs by birthday", e);
            throw new DaoException("error while getting birthday boys");
        }
        return contactList;
    }
}
