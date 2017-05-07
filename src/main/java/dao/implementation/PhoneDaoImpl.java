package dao.implementation;

import dao.interfaces.PhoneDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class PhoneDaoImpl implements PhoneDao {
    private static final Logger LOG = LoggerFactory.getLogger(PhoneDaoImpl.class);
    private final String TABLE_NAME = "\"phone\"";
    private ResultSetMapper<Phone> rsMapper;
    private JdbcTemplate<Phone> jdbcTemplate;

    public PhoneDaoImpl(Connection connection) {
        this.jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            Phone phone = new Phone();
            phone.setId(rs.getInt("id"));
            phone.setCountryID(rs.getInt("id_country"));
            phone.setOperatorCode(rs.getInt("operator_code"));
            phone.setPhoneNumber(rs.getLong("phone_number"));
            phone.setContactID(rs.getInt("id_contact"));
            phone.setType(rs.getBoolean("type"));
            phone.setComment(rs.getString("comment"));
            return phone;
        };
    }

    @Override
    public List<Phone> getByContactID(int contactId) throws DaoException {
        LOG.info("selecting phones by contact id - {}", contactId);
        String sql = String.format("SELECT * FROM %s WHERE \"id_contact\" = ?", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql, contactId);
    }

    @Override
    public void deleteByContactID(int contactID) throws DaoException {
        LOG.info("deleting phones by contact id - {}", contactID);
        String sql = String.format("DELETE FROM %s WHERE \"id_contact\" = ?", TABLE_NAME);
        jdbcTemplate.update(sql, contactID);
    }

    @Override
    public void insert(Phone phone) throws DaoException {
        LOG.info("inserting phone - {}", phone);
        String sql = String.format("INSERT INTO %s (\"id_country\", \"operator_code\", \"phone_number\", \"id_contact\", \"type\", \"comment\") VALUES(?, ?, ?, ?, ?, ?)", TABLE_NAME);
        jdbcTemplate.update(sql, phone.getCountryID(),
                                phone.getOperatorCode(),
                                phone.getPhoneNumber(),
                                phone.getContactID(),
                                phone.getType(),
                                phone.getComment());
    }

    @Override
    public void insert(List<Phone> phoneList) throws DaoException {
        LOG.info("inserting phone list - {}", phoneList);
        if(phoneList.isEmpty())
            return;
        String sql = String.format("INSERT INTO %s (\"id_country\", \"operator_code\", \"phone_number\", \"id_contact\", \"type\", \"comment\") VALUES(?, ?, ?, ?, ?, ?)", TABLE_NAME);
        List<Object[]> args = new ArrayList<>();
        for (Phone phone : phoneList) {
            args.add(new Object[]{phone.getCountryID(),
                    phone.getOperatorCode(),
                    phone.getPhoneNumber(),
                    phone.getContactID(),
                    phone.getType(),
                    phone.getComment()});
        }
        jdbcTemplate.batchUpdate(sql, args);
    }
}
