package service;


import exceptions.DaoException;
import model.UserRoles;

import java.util.List;

public interface UserRolesService {
    List<UserRoles> getByLogin(String login) throws DaoException;
    void insert(UserRoles userRoles) throws DaoException;
}
