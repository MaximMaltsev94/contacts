package service;

import exceptions.DaoException;
import model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    User parseRequest(HttpServletRequest request);
    void deleteProfileImageFile(User user);
    void setHashedPassword(User user, String password);

    User getByLogin(String login) throws DaoException;
    List<User> getByNeedNotify(boolean needBDateNotify) throws DaoException;
    void insert(User user) throws DaoException;
    void update(User user) throws DaoException;
    void delete(String login) throws DaoException;
}
