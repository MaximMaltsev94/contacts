package dao.mysqlimplementation;

import dao.interfaces.ConnectionFactory;
import dao.interfaces.RelationshipDao;
import model.Relationship;
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
 * Created by maxim on 19.09.2016.
 */
public class MySqlRelationshipDao implements RelationshipDao {
    private final static Logger LOG = LoggerFactory.getLogger(MySqlRelationshipDao.class);

    private ConnectionFactory connectionFactory;

    public MySqlRelationshipDao() throws NamingException {
        this.connectionFactory = MySqlConnectionFactory.getInstance();
    }

    @Override
    public List<Relationship> getAll() {
        List<Relationship> relationshipList = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`relationship`");
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Relationship relationship = new Relationship();
                relationship.setId(rs.getInt("id"));
                relationship.setName(rs.getString("name"));
                relationshipList.add(relationship);
            }
        } catch (SQLException e) {
            LOG.warn("can't read relationship list", e);
        }
        return relationshipList;
    }
}
