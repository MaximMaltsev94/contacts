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
    private ConnectionFactory connectionFactory;

    public MySqlPhoneDao() throws NamingException {
        connectionFactory = MySqlConnectionFactory.getInstance();
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

    private PreparedStatement createGetPhoneByContactIDStatement(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`phone` WHERE `id_contact` = ?");
        preparedStatement.setObject(1, id);
        return preparedStatement;
    }

    @Override
    public List<Phone> getPhoneByContactID(int id) {
        List<Phone> phoneList = new ArrayList<>();
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = createGetPhoneByContactIDStatement(connection, id);
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
}
