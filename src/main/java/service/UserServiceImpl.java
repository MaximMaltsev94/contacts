package service;

import dao.implementation.UserDaoImpl;
import dao.interfaces.UserDao;
import exceptions.DaoException;
import model.User;
import util.ContactUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserServiceImpl(Connection connection) {
        this.userDao = new UserDaoImpl(connection);
    }

    @Override
    public User parseRequest(HttpServletRequest request) {
        String login = (String) request.getAttribute("username");
        String email = (String) request.getAttribute("email");
        String password = (String) request.getAttribute("password");
        password = ContactUtils.getSHA256HEX(password);

        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    @Override
    public User getByLogin(String login) throws DaoException {
        return userDao.getByLogin(login);
    }

    @Override
    public void insert(User user) throws DaoException {
        userDao.insert(user);
    }
}
