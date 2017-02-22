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
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final static Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);
    private Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private User parseResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setProfilePicture(rs.getString("profile_picture"));
        user.setNeedBDateNotify(rs.getBoolean("need_bdate_notify"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    private PreparedStatement createGetByLoginStatement(Connection connection, String login) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user` WHERE login = ?");
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

    private PreparedStatement createGetByNeedNotifyStatement(Connection connection, boolean needBDateNotify) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user` WHERE `need_bdate_notify` = ?");
        statement.setObject(1, needBDateNotify);
        LOG.info(statement.toString());
        return statement;
    }

    @Override
    public List<User> getByNeedNotify(boolean needBDateNotify) throws DaoException {
        List<User> userList = new ArrayList<>();
        try(PreparedStatement statement = createGetByNeedNotifyStatement(connection, needBDateNotify);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next())
                userList.add(parseResultSet(rs));
        } catch (SQLException e) {
            LOG.error("can't get user by need birth date notify flag - {}", needBDateNotify, e);
            throw new DaoException("error while getting users by need birth date notify flag", e);
        }
        return userList;
    }

    private void fillPreparedStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setObject(1, user.getLogin());
        statement.setObject(2, user.getEmail());
        statement.setObject(3, user.getNeedBDateNotify());
        statement.setObject(4, user.getProfilePicture());
        statement.setObject(5, user.getPassword());
    }

    @Override
    public void insert(User user) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO `user` (`login`, `email`, `need_bdate_notify`, `profile_picture`, `password`) VALUES(?, ?, ?, ?, ?)")) {
            fillPreparedStatement(statement, user);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't insert user - {}", user, e);
            throw new DaoException("error while insert user " + user, e);
        }
    }

    @Override
    public void update(User user) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement("UPDATE `user` SET `login` = ?, `email` = ?, `need_bdate_notify` = ?, `profile_picture` = ?, `password` = ? WHERE `login` = ?")) {
            fillPreparedStatement(statement, user);
            statement.setObject(6, user.getLogin());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't update user - {}", user, e);
            throw new DaoException("error while updating user " + user, e);
        }
    }

    @Override
    public void delete(String login) throws DaoException {
        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM `user` WHERE `login` = ?")) {
            statement.setObject(1, login);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("can't delete user by login - {}", login, e);
            throw new DaoException("error while deleting user by login " + login, e);
        }
    }
}
