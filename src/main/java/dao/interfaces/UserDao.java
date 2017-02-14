package dao.interfaces;

import exceptions.DaoException;
import model.User;

public interface UserDao {
    User getByLogin(String login) throws DaoException;
    void insert(User user) throws DaoException;
//    void update(User user);
//    void delete(User user);
}
