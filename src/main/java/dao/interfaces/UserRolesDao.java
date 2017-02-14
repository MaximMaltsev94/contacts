package dao.interfaces;

import exceptions.DaoException;
import model.UserRoles;

import java.util.List;


public interface UserRolesDao {
    List<UserRoles> getByLogin(String login) throws DaoException;
    void insert(UserRoles userRoles) throws DaoException;
}
