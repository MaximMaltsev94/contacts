package service;

import dao.implementation.UserDaoImpl;
import dao.interfaces.UserDao;
import exceptions.DaoException;
import model.User;
import util.ContactFileUtils;
import util.ContactUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private ContactService contactService;

    public UserServiceImpl(Connection connection) {
        this.userDao = new UserDaoImpl(connection);
        this.contactService = new ContactServiceImpl(connection);
    }

    @Override
    public User parseRequest(HttpServletRequest request) {
        String login = (String) request.getAttribute("username");
        String email = (String) request.getAttribute("email");
        String profilePicture = contactService.parseProfileImage(request);

        boolean needBDateNotify = false;

        if(request.getAttribute("bdate_notify") != null) {
            needBDateNotify = request.getAttribute("bdate_notify").toString().equalsIgnoreCase("on");
        }

        String password = (String) request.getAttribute("password");
        password = ContactUtils.getSHA256HEX(password);

        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setNeedBDateNotify(needBDateNotify);
        user.setProfilePicture(profilePicture);
        user.setPassword(password);

        return user;
    }

    @Override
    public void deleteProfileImageFile(User user) {
        if(user.getProfilePicture() != null) {
            ContactFileUtils.deleteFileByUrl(user.getProfilePicture(), "pri");
        }
    }

    @Override
    public User getByLogin(String login) throws DaoException {
        return userDao.getByLogin(login);
    }

    @Override
    public List<User> getByNeedNotify(boolean needBDateNotify) throws DaoException {
        return userDao.getByNeedNotify(needBDateNotify);
    }

    @Override
    public void insert(User user) throws DaoException {
        userDao.insert(user);
    }

    @Override
    public void update(User user) throws DaoException {
        userDao.update(user);
    }

    @Override
    public void delete(String login) throws DaoException {
        userDao.delete(login);
    }
}
