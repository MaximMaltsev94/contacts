package dao.implementation;

import dao.interfaces.UserDao;
import exceptions.DaoException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    private final static Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);
    private Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private User parseResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    private PreparedStatement createGetByLoginStatement(Connection connection, String login) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE login = ?");
        statement.setObject(1, login);
        return statement;
    }

    @Override
    public User getByLogin(String login) throws DaoException {
        User user = null;
        try(PreparedStatement statement = createGetByLoginStatement(connection, login);
            ResultSet rs = statement.executeQuery()) {
            if(rs.next()) {
                user = parseResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.error("can't find user by login - {}", login, e);
            throw new DaoException("error while getting user by login " + login, e);
        }
        return user;
    }

    @Override
    public void insert(User user) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO user VALUES(?, ?)")) {
            statement.setObject(1, user.getLogin());
            statement.setObject(2, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't insert user - {}", user, e);
            throw new DaoException("error while insert user " + user, e);
        }
    }
}
