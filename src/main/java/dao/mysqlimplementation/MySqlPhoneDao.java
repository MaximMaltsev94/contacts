package dao.mysqlimplementation;

import dao.interfaces.ConnectionFactory;
import dao.interfaces.PhoneDao;
import model.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxim on 25.09.2016.
 */
public class MySqlPhoneDao implements PhoneDao {
    private final static Logger LOG = LoggerFactory.getLogger(MySqlPhoneDao.class);
    private Connection connection;

    public MySqlPhoneDao(Connection connection) {
        this.connection = connection;
    }

    private Phone parseResultSet(ResultSet rs) throws SQLException {
        Phone phone = new Phone();
        phone.setId(rs.getInt("id"));
        phone.setCountryID(rs.getInt("id_country"));
        phone.setOperatorCode(rs.getInt("operator_code"));
        phone.setPhoneNumber(rs.getLong("phone_number"));
        phone.setContactID(rs.getInt("id_contact"));
        phone.setType(rs.getBoolean("type"));
        phone.setComment(rs.getString("comment"));
        return phone;
    }

    private PreparedStatement createGetPhoneByContactIDStatement(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`phone` WHERE `id_contact` = ?");
        preparedStatement.setObject(1, id);
        return preparedStatement;
    }

    @Override
    public List<Phone> getPhoneByContactID(int id) {
        List<Phone> phoneList = new ArrayList<>();
        try(PreparedStatement preparedStatement = createGetPhoneByContactIDStatement(id);
            ResultSet rs = preparedStatement.executeQuery()){
            while (rs.next()) {
                Phone phone = parseResultSet(rs);
                phoneList.add(phone);
            }

        } catch (SQLException e) {
            LOG.warn("can't get user's phones - {}", id, e);
        }
        return phoneList;
    }

    @Override
    public void deleteByContactID(int contactID) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `contacts_maltsev`.`phone` WHERE `id_contact` = ?");) {
            preparedStatement.setObject(1, contactID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.warn("can't delete phones by contact id ", e);
            throw new SQLException();
        }
    }

    @Override
    public void insert(Phone phone) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `contacts_maltsev`.`phone` (`id_country`, `operator_code`, `phone_number`, `id_contact`, `type`, `comment`) VALUES(?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setObject(1, phone.getCountryID());
            preparedStatement.setObject(2, phone.getOperatorCode());
            preparedStatement.setObject(3, phone.getPhoneNumber());
            preparedStatement.setObject(4, phone.getContactID());
            preparedStatement.setObject(5, phone.getType());
            preparedStatement.setObject(6, phone.getComment());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.warn("can't insert phone ", e);
            throw new SQLException();
        }
    }
}