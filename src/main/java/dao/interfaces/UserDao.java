package dao.interfaces;

import exceptions.DaoException;
import model.User;

import java.util.List;

public interface UserDao {
    User getByLogin(String login) throws DaoException;
    List<User> getByNeedNotify(boolean needBDateNotify) throws DaoException;
    void insert(User user) throws DaoException;
    void update(User user) throws DaoException;
    void delete(String login) throws DaoException;

    User getByEmail(String email) throws DaoException;
}
