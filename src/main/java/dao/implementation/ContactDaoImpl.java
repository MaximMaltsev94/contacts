package dao.implementation;

import dao.interfaces.ContactDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.Contact;
import model.ContactSearchCriteria;
import model.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DaoUtils;

import java.sql.Connection;
import java.util.*;

public class ContactDaoImpl implements ContactDao {
    private static final Logger LOG = LoggerFactory.getLogger(ContactDaoImpl.class);
    private final String TABLE_NAME = "\"contact\"";
    private ResultSetMapper<Contact> rsMapper;
    private JdbcTemplate<Contact> jdbcTemplate;

    public ContactDaoImpl(Connection connection) {
        this.jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            Contact contact = new Contact();

            contact.setId(rs.getInt("id"));
            contact.setFirstName(rs.getString("first_name"));
            contact.setLastName(rs.getString("last_name"));
            contact.setPatronymic(rs.getString("patronymic"));
            contact.setBirthDay(rs.getInt("birth_day"));
            contact.setBirthMonth(rs.getInt("birth_month"));
            contact.setBirthYear(rs.getInt("birth_year"));
            contact.setGender(rs.getInt("gender"));
            contact.setCitizenship(rs.getString("citizenship"));
            contact.setRelationshipID(rs.getByte("id_relationship"));
            contact.setWebSite(rs.getString("web_site"));
            contact.setEmail(rs.getString("email"));
            contact.setCompanyName(rs.getString("company_name"));
            contact.setProfilePicture(rs.getString("profile_picture"));
            contact.setCountryID(rs.getInt("id_country"));
            contact.setCityID(rs.getInt("id_city"));
            contact.setStreet(rs.getString("street"));
            contact.setPostcode(rs.getString("postcode"));
            contact.setVkId(rs.getInt("id_vk"));
            contact.setOkId(rs.getString("id_ok"));
            contact.setFacebookId(rs.getString("id_facebook"));
            contact.setInstagramId(rs.getString("id_instagram"));
            contact.setTwitterId(rs.getString("id_twitter"));
            contact.setYoutubeId(rs.getString("id_youtube"));
            contact.setLinkedinId(rs.getString("id_linkedin"));
            contact.setSkypeId(rs.getString("id_skype"));
            contact.setLoginUser(rs.getString("login_user"));

            return contact;
        };
    }

    @Override
    public Contact insert(Contact contact) throws DaoException {
        LOG.info("inserting contact - {}", contact);
        String sql = String.format("INSERT INTO %s (\"first_name\", \"last_name\", \"patronymic\", \"birth_day\", \"birth_month\", \"birth_year\", \"gender\", \"citizenship\", \"id_relationship\", \"web_site\", \"email\", \"company_name\", \"profile_picture\", \"id_country\", \"id_city\", \"street\", \"postcode\", \"id_vk\", \"id_ok\", \"id_facebook\", \"id_instagram\", \"id_twitter\", \"id_youtube\", \"id_linkedin\", \"id_skype\", \"login_user\") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
        List<Integer> generatedKeys = new ArrayList<>();

        jdbcTemplate.update(sql, generatedKeys, contact.getFirstName(),
                                                contact.getLastName(),
                                                contact.getPatronymic(),
                                                contact.getBirthDay() == 0 ? null : contact.getBirthDay(),
                                                contact.getBirthMonth() == 0 ? null : contact.getBirthMonth(),
                                                contact.getBirthYear() == 0 ? null : contact.getBirthYear(),
                                                contact.getGender(),
                                                contact.getCitizenship(),
                contact.getRelationshipID() == 0 ? null : contact.getRelationshipID(),
                                                contact.getWebSite(),
                                                contact.getEmail(),
                                                contact.getCompanyName(),
                                                contact.getProfilePicture(),
                contact.getCountryID() == 0 ? null : contact.getCountryID(),
                contact.getCityID() == 0 ? null : contact.getCityID(),
                                                contact.getStreet(),
                                                contact.getPostcode(),
                contact.getVkId() == 0 ? null : contact.getVkId(),
                                                contact.getOkId(),
                                                contact.getFacebookId(),
                                                contact.getInstagramId(),
                                                contact.getTwitterId(),
                                                contact.getYoutubeId(),
                                                contact.getLinkedinId(),
                                                contact.getSkypeId(),
                                                contact.getLoginUser());

        contact.setId(generatedKeys.get(0));
        return contact;
    }

    @Override
    public List<Integer> insert(List<Contact> contactList) throws DaoException {
        LOG.info("inserting contact list - {}", contactList);
        String sql = String.format("INSERT INTO %s (\"first_name\", \"last_name\", \"patronymic\", \"birth_day\", \"birth_month\", \"birth_year\", \"gender\", \"citizenship\", \"id_relationship\", \"web_site\", \"email\", \"company_name\", \"profile_picture\", \"id_country\", \"id_city\", \"street\", \"postcode\", \"id_vk\", \"id_ok\", \"id_facebook\", \"id_instagram\", \"id_twitter\", \"id_youtube\", \"id_linkedin\", \"id_skype\", \"login_user\") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
        List<Object[]> args = new ArrayList<>();
        for (Contact contact : contactList) {
            args.add(new Object[] {
                    contact.getFirstName(),
                    contact.getLastName(),
                    contact.getPatronymic(),
                    contact.getBirthDay() == 0 ? null : contact.getBirthDay(),
                    contact.getBirthMonth() == 0 ? null : contact.getBirthMonth(),
                    contact.getBirthYear() == 0 ? null : contact.getBirthYear(),
                    contact.getGender(),
                    contact.getCitizenship(),
                    contact.getRelationshipID() == 0 ? null : contact.getRelationshipID(),
                    contact.getWebSite(),
                    contact.getEmail(),
                    contact.getCompanyName(),
                    contact.getProfilePicture(),
                    contact.getCountryID() == 0 ? null : contact.getCountryID(),
                    contact.getCityID() == 0 ? null : contact.getCityID(),
                    contact.getStreet(),
                    contact.getPostcode(),
                    contact.getVkId() == 0 ? null : contact.getVkId(),
                    contact.getOkId(),
                    contact.getFacebookId(),
                    contact.getInstagramId(),
                    contact.getTwitterId(),
                    contact.getYoutubeId(),
                    contact.getLinkedinId(),
                    contact.getSkypeId(),
                    contact.getLoginUser()});
        }
        List<Integer> generatedKeys = new ArrayList<>();
        jdbcTemplate.batchUpdate(sql, generatedKeys, args);
        return generatedKeys;
    }

    @Override
    public void update(Contact contact) throws DaoException {
        LOG.info("updating contact - {}", contact);
        String sql = String.format("UPDATE %s SET \"first_name\" = ?, \"last_name\" = ?, \"patronymic\" = ?, \"birth_day\" = ?, \"birth_month\" = ?, \"birth_year\" = ?, \"gender\" = ?,\"citizenship\" = ?, \"id_relationship\" = ?, \"web_site\" = ?, \"email\" = ?, \"company_name\" = ?, \"profile_picture\" = ?, \"id_country\" = ?, \"id_city\" = ?, \"street\" = ?, \"postcode\" = ?, \"id_vk\" = ?, \"id_ok\" = ?, \"id_facebook\" = ?, \"id_instagram\" = ?, \"id_twitter\" = ?, \"id_youtube\" = ?, \"id_linkedin\" = ?, \"id_skype\" = ?, \"login_user\" = ? WHERE \"id\" = ?", TABLE_NAME);

        String birthDate = null;
        jdbcTemplate.update(sql, contact.getFirstName(),
                                    contact.getLastName(),
                                    contact.getPatronymic(),
                                    contact.getBirthDay() == 0 ? null : contact.getBirthDay(),
                                    contact.getBirthMonth() == 0 ? null : contact.getBirthMonth(),
                                    contact.getBirthYear() == 0 ? null : contact.getBirthYear(),
                                    contact.getGender(),
                                    contact.getCitizenship(),
                                    contact.getRelationshipID() == 0 ? null : contact.getRelationshipID(),
                                    contact.getWebSite(),
                                    contact.getEmail(),
                                    contact.getCompanyName(),
                                    contact.getProfilePicture(),
                                    contact.getCountryID() == 0 ? null : contact.getCountryID(),
                                    contact.getCityID() == 0 ? null : contact.getCityID(),
                                    contact.getStreet(),
                                    contact.getPostcode(),
                                    contact.getVkId() == 0 ? null : contact.getVkId(),
                                    contact.getOkId(),
                                    contact.getFacebookId(),
                                    contact.getInstagramId(),
                                    contact.getTwitterId(),
                                    contact.getYoutubeId(),
                                    contact.getLinkedinId(),
                                    contact.getSkypeId(),
                                    contact.getLoginUser(),
                                    contact.getId());
    }

    @Override
    public void delete(Contact contact) throws DaoException {
        LOG.info("deleting contact - {}", contact);
        deleteByID(contact.getId());
    }

    @Override
    public void deleteByID(int id) throws DaoException {
        LOG.info("deleting contact by id - {}", id);
        String sql = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void delete(List<Integer> idList) throws DaoException {
        LOG.info("deleting contacts by id list - {}", idList);
        if(idList.isEmpty())
            return;

        String sql = String.format("DELETE FROM %s WHERE \"id\" %s", TABLE_NAME, DaoUtils.generateSqlInPart(idList.size()));
        jdbcTemplate.update(sql, idList.toArray());
    }

    @Override
    public Contact getByIDAndLoginUser(int id, String loginUser) throws DaoException {
        LOG.info("selecting contact by id - {} and login user - {}", id, loginUser);
        String sql = String.format("SELECT * FROM %s WHERE id = ? and login_user = ?", TABLE_NAME);
        return jdbcTemplate.queryForObject(rsMapper, sql, id, loginUser);
    }

    @Override
    public List<Contact> getByIdInAndLoginUser(List<Integer> idList, String loginUser) throws DaoException {
        LOG.info("selecting contacts by id list - {} and login user - {}", idList, loginUser);
        if(idList.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = String.format("SELECT * FROM %s WHERE \"login_user\" = ? and \"id\" %s", TABLE_NAME, DaoUtils.generateSqlInPart(idList.size()));
        List<Object> params = new ArrayList<>();
        params.add(loginUser);
        params.addAll(idList);
        return jdbcTemplate.queryForList(rsMapper, sql, params.toArray());
    }

    @Override
    public Page<Contact> getByIdInAndLoginUser(int pageNumber, int limit, List<Integer> idList, String loginUser) throws DaoException {
        LOG.info("selecting contacts page - {} limit - {} by id list - {} and login user - {}", pageNumber, limit, idList, limit);
        if(idList.isEmpty()){
            return new Page<>(Collections.emptyList(), 1, 0);
        }
        String sql = String.format("SELECT *, count(*) OVER() total_count FROM %s WHERE \"login_user\" = ? and \"id\" %s ORDER BY \"id\" desc OFFSET ? LIMIT ?", TABLE_NAME, DaoUtils.generateSqlInPart(idList.size()));
        List<Object> params = new ArrayList<>();
        params.add(loginUser);
        params.addAll(idList);
        params.add((pageNumber - 1) * limit);
        params.add(limit);
        return jdbcTemplate.queryForPage(rsMapper, sql, pageNumber, params.toArray());
    }

    @Override
    public long getMaxID() throws DaoException {
        LOG.info("selecting max id from contacts table");
        String sql = String.format("SELECT MAX(\"id\") AS mx FROM %s", TABLE_NAME);
        Contact contact = new Contact();
        jdbcTemplate.queryForObject(rs -> {
            contact.setId(rs.getInt("mx"));
            return contact;
        }, sql);
        return contact.getId();
    }

    @Override
    public List<Contact> getByLoginUser(String loginUser) throws DaoException {
        LOG.info("selecting contacts by login user - {}", loginUser);
        String sql = String.format("SELECT * FROM %s where \"login_user\" = ?", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql, loginUser);
    }

    @Override
    public Page<Contact> getByLoginUser(int pageNumber, int limit, String loginUser) throws DaoException {
        LOG.info("selecting contacts page - {} limit - {} by login user - {}", pageNumber, limit, loginUser);
        String sql = String.format("SELECT *, count(*) OVER() as total_count FROM %s WHERE \"login_user\" = ? ORDER BY \"id\" desc OFFSET ? LIMIT ?", TABLE_NAME);
        return jdbcTemplate.queryForPage(rsMapper, sql, pageNumber, loginUser, (pageNumber - 1) * limit, limit);
    }

    @Override
    public long getCountByLoginUser(String loginUser) throws DaoException {
        LOG.info("selecting contacts count by login user - {}", loginUser);
        String sql = String.format("SELECT COUNT(\"id\") AS \"cnt\" FROM %s WHERE \"login_user\" = ?", TABLE_NAME);
        Contact contact = new Contact();
        jdbcTemplate.queryForObject(rs -> {
            contact.setId(rs.getInt("cnt"));
            return contact;
        }, sql);
        return contact.getId();
    }

    private String surroundPercentString(String val) {
        return StringUtils.isBlank(val) ? "%" : "%" + val.toLowerCase() + "%";
    }

    private String surroundPercentInt(int val, int template) {
        return val == template ? "%" : "%" + val + "%";
    }

    @Override
    public Page<Contact> getByLoginUser(ContactSearchCriteria searchCriteria, int pageNumber, int limit, String loginUser) throws DaoException {
        LOG.info("searching contacts by criteria - {}", searchCriteria);
        String sql = String.format("SELECT *, count(*) OVER() as total_count FROM %s WHERE \"login_user\" = ? and " +
                "first_name ilike ? and " +
                "last_name ilike ? and " +
                "coalesce(patronymic, '') ilike ? and " +
                "coalesce(cast(make_date(birth_year, birth_month, birth_day) between (current_date) - make_interval(years := ?) and current_date - make_interval(years := ?) as char), 'f') = 't' and " +
                "cast(gender as char) ilike ? and " +
                "coalesce(citizenship, '') ilike ? and " +
                "coalesce(cast(id_relationship as char), '') ilike ? and " +
                "coalesce(company_name, '') ilike ? and " +
                "coalesce(cast(id_country as char), '') ilike ? and " +
                "coalesce(cast(id_city as char), '') ilike ? and " +
                "coalesce(street, '') ilike ? and " +
                "coalesce(postcode, '') ilike ? OFFSET ? LIMIT ?", TABLE_NAME);


        int age1 = searchCriteria.getAge1();
        int age2 = searchCriteria.getAge2();

        if (age1 == 0 && age2 == 0) {
            age1 = -1;
            age2 = 200;
        }

        return jdbcTemplate.queryForPage(rsMapper, sql, pageNumber,
                loginUser,
                surroundPercentString(searchCriteria.getFirstName()),
                surroundPercentString(searchCriteria.getLastName()),
                surroundPercentString(searchCriteria.getPatronymic()),
                age2,
                age1,
                surroundPercentInt(searchCriteria.getGender(), 0),
                surroundPercentString(searchCriteria.getCitizenship()),
                surroundPercentInt(searchCriteria.getRelationship(), 0),
                surroundPercentString(searchCriteria.getCompanyName()),
                surroundPercentInt(searchCriteria.getCountry(), 0),
                surroundPercentInt(searchCriteria.getCity(), 0),
                surroundPercentString(searchCriteria.getStreet()),
                surroundPercentString(searchCriteria.getPostcode()),
                (pageNumber - 1) * limit,
                limit);
    }

    @Override
    public List<Contact> getByEmailNotNullAndLoginUser(String loginUser) throws DaoException {
        LOG.info("selecting contacts with not null email and login user - {}", loginUser);
        String sql = String.format("SELECT * FROM %s WHERE \"login_user\" = ? and \"email\" IS NOT NULL", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql, loginUser);
    }

    @Override
    public List<Contact> getByBirthdayAndLoginUserIn(Date date, List<String> loginUserList) throws DaoException {
        LOG.info("selecting contacts by birth date - {} and login user list - {}", date, loginUserList);
        String sql = String.format("SELECT * FROM %s WHERE \"birth_day\" = ? and \"birth_month\" = ? and \"login_user\" %s", TABLE_NAME, DaoUtils.generateSqlInPart(loginUserList.size()));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        List<Object> params = new ArrayList<>();
        params.add(cal.get(Calendar.DAY_OF_MONTH));
        params.add(cal.get(Calendar.MONTH) + 1);
        params.addAll(loginUserList);

        return jdbcTemplate.queryForList(rsMapper, sql, params.toArray());
    }

    @Override
    public List<Contact> getByVkIdNotNullAndLoginUser(String loginUser) throws DaoException {
        LOG.info("selecting contacts with non null email and login user - {}", loginUser);
        String sql = String.format("SELECT * FROM %s WHERE \"id_vk\" is not null and \"login_user\" = ?", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql, loginUser);
    }
}
