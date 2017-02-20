package service;

import exceptions.DaoException;
import model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    User parseRequest(HttpServletRequest request);
    void deleteProfileImageFile(User user);

    User getByLogin(String login) throws DaoException;
    void insert(User user) throws DaoException;
    void update(User user) throws DaoException;
}
