package service;

import exceptions.DaoException;
import model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    User parseRequest(HttpServletRequest request);

    User getByLogin(String login) throws DaoException;
    void insert(User user) throws DaoException;
}
