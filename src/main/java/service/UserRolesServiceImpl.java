package service;

import dao.implementation.UserRolesDaoImpl;
import dao.interfaces.UserRolesDao;
import exceptions.DaoException;
import model.UserRoles;

import java.sql.Connection;
import java.util.List;

public class UserRolesServiceImpl implements UserRolesService {

    private UserRolesDao userRolesDao;

    public UserRolesServiceImpl(Connection connection) {
        this.userRolesDao = new UserRolesDaoImpl(connection);
    }

    @Override
    public List<UserRoles> getByLogin(String login) throws DaoException {
        return userRolesDao.getByLogin(login);
    }

    @Override
    public void insert(UserRoles userRoles) throws DaoException {
        userRolesDao.insert(userRoles);
    }
}
