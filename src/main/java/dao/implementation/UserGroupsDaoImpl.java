package dao.implementation;

import dao.interfaces.UserGroupsDao;
import exceptions.DaoException;
import model.UserGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserGroupsDaoImpl implements UserGroupsDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserGroupsDaoImpl.class);
    private Connection connection;

    public UserGroupsDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private UserGroups parseResultSet(ResultSet rs) throws SQLException {
        UserGroups userGroups = new UserGroups();
        userGroups.setId(rs.getInt("id"));
        userGroups.setGroupName(rs.getString("group_name"));
        userGroups.setLogin(rs.getString("login"));
        return userGroups;
    }

    private void fillPreparedStatement(PreparedStatement statement, UserGroups userGroups) throws SQLException {
        statement.setObject(1, userGroups.getGroupName());
        statement.setObject(2, userGroups.getLogin());
    }

    private PreparedStatement createGetByLoginStatement(String login) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user_groups` WHERE `login` = ?");
        statement.setObject(1, login);
        return statement;
    }

    @Override
    public List<UserGroups> getByLogin(String login) throws DaoException {
        List<UserGroups> userGroupsList = new ArrayList<>();
        try(PreparedStatement statement = createGetByLoginStatement(login);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                userGroupsList.add(parseResultSet(rs));
            }
        } catch (SQLException e) {
            LOG.error("can't select user groups by login - {}", login, e);
            throw new DaoException("error while getting user groups", e);
        }
        return userGroupsList;
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
