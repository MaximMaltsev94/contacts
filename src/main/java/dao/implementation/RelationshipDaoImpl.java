package dao.implementation;

import dao.interfaces.RelationshipDao;
import exceptions.DaoException;
import model.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RelationshipDaoImpl implements RelationshipDao {
    private static final Logger LOG = LoggerFactory.getLogger(RelationshipDaoImpl.class);

    private Connection connection;

    public RelationshipDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private Relationship parseResultSet(ResultSet rs) throws SQLException {
        Relationship relationship = new Relationship();
        relationship.setId(rs.getInt("id"));
        relationship.setName(rs.getString("name"));
        return relationship;
    }

    @Override
    public List<Relationship> getAll() throws DaoException {
        List<Relationship> relationshipList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `relationship`");
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Relationship relationship = parseResultSet(rs);
                relationshipList.add(relationship);
            }
        } catch (SQLException e) {
            LOG.error("can't read relationship list", e);
            throw new DaoException("error while getting relationship list", e);
        }
        return relationshipList;
    }

    private PreparedStatement createGetByIDStatement(int relationshipID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `relationship` WHERE `id` = ?");
        preparedStatement.setObject(1, relationshipID);
        return preparedStatement;
    }

    @Override
    public Relationship getByID(int relationshipID) throws DaoException {
        Relationship relationship = null;
        try (PreparedStatement preparedStatement = createGetByIDStatement(relationshipID);
                ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                relationship = parseResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.error("can't get relationship by id - {}", relationshipID, e);
            throw new DaoException("error while getting relationship by Id", e);
        }
        return relationship;
    }
}
