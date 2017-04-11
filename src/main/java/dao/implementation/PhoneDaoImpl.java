package dao.implementation;

import dao.interfaces.PhoneDao;
import exceptions.DaoException;
import model.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhoneDaoImpl implements PhoneDao {
    private static final Logger LOG = LoggerFactory.getLogger(PhoneDaoImpl.class);
    private Connection connection;

    public PhoneDaoImpl(Connection connection) {
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
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `phone` WHERE `id_contact` = ?");
        preparedStatement.setObject(1, id);
        return preparedStatement;
    }

    @Override
    public List<Phone> getByContactID(int contactId) throws DaoException {
        List<Phone> phoneList = new ArrayList<>();
        try(PreparedStatement preparedStatement = createGetPhoneByContactIDStatement(contactId);
            ResultSet rs = preparedStatement.executeQuery()){
            while (rs.next()) {
                Phone phone = parseResultSet(rs);
                phoneList.add(phone);
            }

        } catch (SQLException e) {
            LOG.error("can't get phones by contactID - {}", contactId, e);
            throw new DaoException("error while getting phones by contact ID",e);
        }
        return phoneList;
    }

    @Override
    public void deleteByContactID(int contactID) throws DaoException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `phone` WHERE `id_contact` = ?");) {
            preparedStatement.setObject(1, contactID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't delete phones by contact id - {} ", contactID, e);
            throw new DaoException("error while deleting phones by contact ID", e);
        }
    }

    @Override
    public void insert(Phone phone) throws DaoException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `phone` (`id_country`, `operator_code`, `phone_number`, `id_contact`, `type`, `comment`) VALUES(?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setObject(1, phone.getCountryID());
            preparedStatement.setObject(2, phone.getOperatorCode());
            preparedStatement.setObject(3, phone.getPhoneNumber());
            preparedStatement.setObject(4, phone.getContactID());
            preparedStatement.setObject(5, phone.getType());
            preparedStatement.setObject(6, phone.getComment());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't insert phone - {}", phone, e);
            throw new DaoException("error while insertion phone", e);
        }
    }

    @Override
    public void insert(List<Phone> phoneList) throws DaoException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `phone` (`id_country`, `operator_code`, `phone_number`, `id_contact`, `type`, `comment`) VALUES(?, ?, ?, ?, ?, ?)")) {
            for (Phone phone : phoneList) {
                preparedStatement.setObject(1, phone.getCountryID());
                preparedStatement.setObject(2, phone.getOperatorCode());
                preparedStatement.setObject(3, phone.getPhoneNumber());
                preparedStatement.setObject(4, phone.getContactID());
                preparedStatement.setObject(5, phone.getType());
                preparedStatement.setObject(6, phone.getComment());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            LOG.error("can't insert phones list - {}", phoneList, e);
            throw new DaoException("error while inserting phone list", e);
        }
    }
}
