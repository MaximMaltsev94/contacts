package service;

import dao.implementation.UserGroupsDaoImpl;
import dao.interfaces.UserGroupsDao;
import exceptions.DaoException;
import model.UserGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class UserGroupsServiceImpl implements UserGroupsService {
    private static final Logger LOG = LoggerFactory.getLogger(UserGroupsServiceImpl.class);
    private UserGroupsDao userGroupsDao;

    public UserGroupsServiceImpl(Connection connection) {
        this.userGroupsDao = new UserGroupsDaoImpl(connection);
    }

    @Override
    public List<UserGroups> getByLogin(String login) throws DaoException {
        return userGroupsDao.getByLogin(login);
    }

    @Override
    public UserGroups getByIdAndLoginUser(int id, String loginUser) throws DaoException {
        return userGroupsDao.getByIdAndLoginUser(id, loginUser);
    }

    @Override
    public List<UserGroups> getByIdIn(List<Integer> idList) throws DaoException {
        return userGroupsDao.getByIdIn(idList);
    }

    @Override
    public UserGroups insert(UserGroups userGroups) throws DaoException {
        return userGroupsDao.insert(userGroups);
    }

    @Override
    public void update(UserGroups userGroups) throws DaoException {
        userGroupsDao.update(userGroups);
    }
}
