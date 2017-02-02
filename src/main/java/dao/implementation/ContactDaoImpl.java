package dao.implementation;

import dao.interfaces.ContactDao;
import exceptions.DaoException;
import model.Contact;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

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
    public void insert(Contact contact) throws DaoException {
        try (PreparedStatement pStatement = connection.prepareStatement("INSERT INTO `contact` (`first_name`, `last_name`, `patronymic`, `birth_date`, `gender`, `citizenship`, `id_relationship`, `web_site`, `email`, `company_name`, `profile_picture`, `id_country`, `id_city`, `street`, `postcode`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            fillPreparedStatement(pStatement, contact);
            pStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't insert contact - {}", contact, e);
            throw new DaoException("error while inserting contact", e);
        }

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
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contact` ORDER BY `id` desc LIMIT ?, 10");
        preparedStatement.setObject(1, (pageNumber - 1) * limit);
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

    private void appendString(StringBuilder where, String fieldName, String value) {
        if (StringUtils.isNotBlank(value)) {
            where.append(" and LOWER(");
            where.append(fieldName);
            where.append(") LIKE '%");
            where.append(StringUtils.lowerCase(value));
            where.append("%' ");
        }
    }

    private void appendInt(StringBuilder where, String fieldName, int value) {
        where.append(" and ");
        where.append(fieldName);
        where.append(" = ");
        where.append(value);
    }

    private String concatQuery(String firstName, String lastName, String patronymic, int age1, int age2, int gender, String citizenship, int relationship, String companyName, int country, int city, String street, String postcode) {
        StringBuilder searchQuery = new StringBuilder("SELECT * FROM `contact`");
        StringBuilder where = new StringBuilder();

        appendString(where, "first_name", firstName);
        appendString(where, "last_name", lastName);
        appendString(where, "patronymic", patronymic);

        if(!(age1 == 0 && age2 == 0)) {
            if (age2 == 0) age2 = 200;
            where.append("and timestampdiff(YEAR, birth_date, current_date()) between ");
            where.append(age1);
            where.append(" and ");
            where.append(age2);
        }

        if(gender != 2) {
            appendInt(where, "gender", gender);
        }
        appendString(where, "citizenship", citizenship);
        if(relationship != 0)
            appendInt(where, "id_relationship", relationship);
        appendString(where, "company_name", companyName);
        if(country != 0)
            appendInt(where, "id_country", country);
        if(city != 0)
            appendInt(where, "id_city", city);
        appendString(where, "street", street);
        appendString(where, "postcode", postcode);

        String whereStr = where.toString();

        if(StringUtils.contains(whereStr, "and")) {
            searchQuery.append(" WHERE ");
            searchQuery.append(StringUtils.substringAfter(whereStr, "and"));
        }

        return searchQuery.toString();
    }

    @Override
    public List<Contact> find(String firstName, String lastName, String patronymic, int age1, int age2, int gender, String citizenship, int relationship, String companyName, int country, int city, String street, String postcode) throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        String searchQuery = concatQuery(firstName, lastName, patronymic, age1, age2, gender, citizenship, relationship, companyName, country, city, street, postcode);
        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(searchQuery)) {
            while (rs.next()) {
                Contact contact = parseResultSet(rs);
                contactList.add(contact);
            }
        } catch (SQLException e) {
            LOG.error("can't perform search query - {}", searchQuery, e);
            throw new DaoException("error while searching contacts by critetia", e);
        }
        return contactList;
    }

    @Override
    public List<Contact> getContactsWithEmail() throws DaoException {
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
