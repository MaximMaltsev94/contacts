package dao.implementation;

import dao.interfaces.ContactDao;
import exceptions.DaoException;
import model.Contact;
import model.ContactSearchCriteria;
import model.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        contact.setLoginUser(rs.getString("login_user"));

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
        preparedStatement.setObject(16, contact.getLoginUser());

    }

    public ContactDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Contact insert(Contact contact) throws DaoException {
        try (PreparedStatement pStatement = connection.prepareStatement("INSERT INTO `contact` (`first_name`, `last_name`, `patronymic`, `birth_date`, `gender`, `citizenship`, `id_relationship`, `web_site`, `email`, `company_name`, `profile_picture`, `id_country`, `id_city`, `street`, `postcode`, `login_user`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
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
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `contact` SET `first_name` = ?, `last_name` = ?, `patronymic` = ?, `birth_date` = ?, `gender` = ?,`citizenship` = ?, `id_relationship` = ?, `web_site` = ?, `email` = ?, `company_name` = ?, `profile_picture` = ?, `id_country` = ?, `id_city` = ?, `street` = ?, `postcode` = ?, `login_user` = ? WHERE `id` = ?");) {
            fillPreparedStatement(preparedStatement, contact);
            preparedStatement.setObject(17, contact.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't update contact - {}", contact, e);
            throw new DaoException();
        }

    }

    @Override
    public void delete(Contact contact) throws DaoException {
        deleteByID(contact.getId());
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

    private PreparedStatement createGetByIDStatement(Connection connection, int id, String loginUser) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contact` WHERE id = ? and login_user = ?");
        preparedStatement.setObject(1, id);
        preparedStatement.setObject(2, loginUser);
        return preparedStatement;
    }

    @Override
    public Contact getByIDAndLoginUser(int id, String loginUser) throws DaoException {
        Contact contact = null;
        try (PreparedStatement preparedStatement = createGetByIDStatement(connection, id, loginUser);
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
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM `contact` WHERE `login_user` = ? and `id` in (");
        for(int i = 0; i < size; ++i) {
            sqlBuilder.append(" ?");
            if(i != size - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    private PreparedStatement createGetByIdInStatement(Connection connection, List<Integer> idList, String loginUser) throws SQLException {
        String sql = createGetByIdInSQL(idList.size());

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setObject(1, loginUser);
        for (int i = 0; i < idList.size(); i++) {
            statement.setObject(i + 2, idList.get(i));
        }
        return statement;
    }


    @Override
    public List<Contact> getByIdInAndLoginUser(List<Integer> idList, String loginUser) throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        if(idList.isEmpty()) {
            return contactList;
        }

        try(PreparedStatement statement = createGetByIdInStatement(connection, idList, loginUser);
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

    private PreparedStatement createGetByLoginUserStatement(Connection connection, String loginUser) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `contact` where `login_user` = ?");
        statement.setObject(1, loginUser);
        return statement;
    }

    @Override
    public List<Contact> getByLoginUser(String loginUser) throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        try(PreparedStatement preparedStatement = createGetByLoginUserStatement(connection, loginUser);
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                contactList.add(parseResultSet(rs));
            }

        } catch (SQLException e) {
            LOG.error("can't get contacts by login user - {}", loginUser, e);
            throw new DaoException("error while getting contacts by login user" + loginUser, e);
        }
        return contactList;
    }

    private PreparedStatement createGetContactsPageStatement(Connection connection, int pageNumber, int limit, String loginUser) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT SQL_CALC_FOUND_ROWS * FROM `contact` WHERE `login_user` = ? ORDER BY `id` desc LIMIT ?, ?");
        preparedStatement.setObject(1, loginUser);
        preparedStatement.setObject(2, (pageNumber - 1) * limit);
        preparedStatement.setObject(3, limit);
        return preparedStatement;
    }

    @Override
    public Page<Contact> getByLoginUser(int pageNumber, int limit, String loginUser) throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        int totalRowCount = 1;
        try (PreparedStatement preparedStatement = createGetContactsPageStatement(connection, pageNumber, limit, loginUser);
             ResultSet rs = preparedStatement.executeQuery();
             PreparedStatement statement = connection.prepareStatement("SELECT found_rows()");
             ResultSet found_rows = statement.executeQuery()) {
            while (rs.next()) {
                Contact contact = parseResultSet(rs);
                contactList.add(contact);
            }
            if(found_rows.next()) {
                totalRowCount = found_rows.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("can't get contacts list by page number - {}", pageNumber, e);
            throw new DaoException("error while getting contact page", e);
        }
        return new Page<>(contactList, pageNumber, totalRowCount);
    }

    private PreparedStatement createGetCountStatement(Connection connection, String loginUser) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(`id`) AS `cnt` FROM `contact` WHERE `login_user` = ?");
        statement.setObject(1, loginUser);
        return statement;
    }

    @Override
    public long getCountByLoginUser(String loginUser) throws DaoException {
        long count = 0;
        try (PreparedStatement preparedStatement = createGetCountStatement(connection, loginUser);
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

    private PreparedStatement concatSearchQuery(Connection connection, ContactSearchCriteria searchCriteria, int pageNumber, int limit, String loginUser) throws SQLException {
        String sql = "SELECT SQL_CALC_FOUND_ROWS * FROM `contact` WHERE `login_user` = ? and " +
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
        statement.setObject(1, loginUser);
        statement.setObject(2, surroundPercentString(searchCriteria.getFirstName()));
        statement.setObject(3, surroundPercentString(searchCriteria.getLastName()));
        statement.setObject(4, surroundPercentString(searchCriteria.getPatronymic()));

        int age1 = searchCriteria.getAge1();
        int age2 = searchCriteria.getAge2();

        if(age1 == 0 && age2 == 0) {
            age1 = -1;
            age2 = 200;
        }

        statement.setObject(5, age1);
        statement.setObject(6, age2);
        statement.setObject(7, surroundPercentInt(searchCriteria.getGender(), 2));
        statement.setObject(8, surroundPercentString(searchCriteria.getCitizenship()));
        statement.setObject(9, surroundPercentInt(searchCriteria.getRelationship(), 0));
        statement.setObject(10, surroundPercentString(searchCriteria.getCompanyName()));
        statement.setObject(11, surroundPercentInt(searchCriteria.getCountry(), 0));
        statement.setObject(12, surroundPercentInt(searchCriteria.getCity(), 0));
        statement.setObject(13, surroundPercentString(searchCriteria.getStreet()));
        statement.setObject(14, surroundPercentString(searchCriteria.getPostcode()));
        statement.setObject(15, (pageNumber - 1) * limit);
        statement.setObject(16, limit);

        LOG.info("search query: " + statement);
        return statement;
    }

    @Override
    public Page<Contact> getByLoginUser(ContactSearchCriteria searchCriteria, int pageNumber, int limit, String loginUser) throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        int totalRowCount = 1;
        try(PreparedStatement statement = concatSearchQuery(connection, searchCriteria, pageNumber, limit, loginUser);
            ResultSet rs = statement.executeQuery();
            PreparedStatement statement1 = connection.prepareStatement("SELECT found_rows()");
            ResultSet foundRows = statement1.executeQuery()) {
            while (rs.next()) {
                Contact contact = parseResultSet(rs);
                contactList.add(contact);
            }
            if(foundRows.next()) {
                totalRowCount = foundRows.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("can't perform search query", e);
            throw new DaoException("error while searching contacts by criteria", e);
        }

        return new Page<>(contactList, pageNumber, totalRowCount);
    }

    private PreparedStatement createGetByEmailNotNullStatement(Connection connection, String loginUser) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `contact` WHERE `login_user` = ? and `email` IS NOT NULL");
        statement.setObject(1, loginUser);
        return statement;
    }

    @Override
    public List<Contact> getByEmailNotNullAndLoginUser(String loginUser) throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        try(PreparedStatement preparedStatement = createGetByEmailNotNullStatement(connection, loginUser);
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

    private String createGetByLoginUserInSQL(int size) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM `contact` WHERE day(`birth_date`) = ? and month(`birth_date`) = ? and `login_user` in (");
        for(int i = 0; i < size; ++i) {
            sqlBuilder.append(" ?");
            if(i != size - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    private PreparedStatement createGetByLoginUserInStatement(Connection connection, Date date, List<String> loginUserList) throws SQLException {
        String sql = createGetByLoginUserInSQL(loginUserList.size());

        PreparedStatement statement = connection.prepareStatement(sql);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        statement.setObject(1, cal.get(Calendar.DAY_OF_MONTH));
        statement.setObject(2, cal.get(Calendar.MONTH) + 1);
        for (int i = 0; i < loginUserList.size(); i++) {
            statement.setObject(i + 3, loginUserList.get(i));
        }
        LOG.info(statement.toString());
        return statement;
    }

    @Override
    public List<Contact> getByBirthdayAndLoginUserIn(Date date, List<String> loginUserList) throws DaoException {
        List<Contact> contactList = new ArrayList<>();
        try(PreparedStatement preparedStatement = createGetByLoginUserInStatement(connection, date, loginUserList);
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Contact contact = parseResultSet(rs);
                contactList.add(contact);
            }
        } catch (SQLException e) {
            LOG.error("can't get contacts by birthday", e);
            throw new DaoException("error while getting birthday boys");
        }
        return contactList;
    }
}
