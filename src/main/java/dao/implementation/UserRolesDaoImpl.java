package dao.implementation;

import dao.interfaces.UserRolesDao;
import exceptions.DaoException;
import model.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRolesDaoImpl implements UserRolesDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserRolesDaoImpl.class);
    private Connection connection ;

    public UserRolesDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private UserRoles parseResultSet(ResultSet rs) throws SQLException {
        UserRoles userRoles = new UserRoles();
        userRoles.setLogin(rs.getString("login"));
        userRoles.setRoleName(rs.getString("role_name"));
        return userRoles;
    }

    private PreparedStatement createGetByLoginStatement(Connection connection, String login) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_roles WHERE login = ?");
        statement.setObject(1, login);
        return statement;
    }

    @Override
    public List<UserRoles> getByLogin(String login) throws DaoException {
        List<UserRoles> userRolesList = new ArrayList<>();
        try(PreparedStatement statement = createGetByLoginStatement(connection, login);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                userRolesList.add(parseResultSet(rs));
            }

        } catch (SQLException e) {
            LOG.error("can't get user roles by login - {}", login, e);
            throw new DaoException("error while getting user roles by login - " + login, e);
        }
        return userRolesList;
    }

    @Override
    public void insert(UserRoles userRoles) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO user_roles VALUES(?, ?)")) {
            statement.setObject(1, userRoles.getLogin());
            statement.setObject(2, userRoles.getRoleName());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't insert user role - {}", userRoles, e);
            throw new DaoException("error while inserting user role - " + userRoles, e);
        }
    }
}
