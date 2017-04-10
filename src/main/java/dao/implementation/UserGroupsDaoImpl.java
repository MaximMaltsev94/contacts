package dao.implementation;

import dao.interfaces.UserGroupsDao;
import exceptions.DaoException;
import model.UserGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserGroupsDaoImpl implements UserGroupsDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserGroupsDaoImpl.class);
    private Connection connection;

    public UserGroupsDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private void fillPreparedStatement(PreparedStatement statement, UserGroups userGroups) throws SQLException {
        statement.setObject(1, userGroups.getGroupName());
        statement.setObject(2, userGroups.getLogin());
    }

    @Override
    public void insert(UserGroups userGroups) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO `user_groups` (`group_name`, `login`) VALUES (?, ?)")){
            fillPreparedStatement(statement, userGroups);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't insert user group - {}", userGroups, e);
            throw new DaoException("error while inserting user group", e);
        }
    }
}
