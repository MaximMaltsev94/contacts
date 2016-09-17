package dao.mysqlimplementation;

import dao.interfaces.ConnectionFactory;
import dao.interfaces.ContactDao;
import model.Contact;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlContactDao implements ContactDao {
    ConnectionFactory connectionFactory;

    public MySqlContactDao() throws NamingException {
        connectionFactory = MySqlConnectionFactory.getInstance();
    }

    @Override
    public void insert(Contact contact) {

    }

    @Override
    public void update(Contact contact) {

    }

    @Override
    public void delete(Contact contact) {

    }

    public List<Contact> getContactsPage(int pageNumber) {
        List<Contact> contactList = new ArrayList<>();
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contact` LIMIT ?, 20")) {
            preparedStatement.setInt(1, (pageNumber - 1) * 20);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact();

                contact.setId(rs.getInt("id"));
                contact.setFirstName(rs.getString("first_name" ));
                contact.setLastName(rs.getString("last_name"));
                contact.setPatronymic(rs.getString("patronymic"));
                contact.setBirthDate(rs.getDate("birth_date"));
                contact.setGender(rs.getBoolean("gender"));
                contact.setCitizenshipID(rs.getByte("id_citizenship"));
                contact.setRelationshipID(rs.getByte("id_relationship"));
                contact.setWebSite(rs.getString("web_site"));
                contact.setEmail(rs.getString("email"));
                contact.setCompanyName(rs.getString("company_name"));
                contact.setProfilePicture(rs.getString("profile_picture"));
                contact.setCountyID(rs.getByte("id_country"));
                contact.setCityID(rs.getByte("id_city"));
                contact.setStreet(rs.getString("street"));
                contact.setPostcode(rs.getString("postcode"));

                contactList.add(contact);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    @Override
    public int getRowsCount() {
        int count = 0;
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(`id`) AS `cnt` FROM `contact`")) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                count = rs.getInt("cnt");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
