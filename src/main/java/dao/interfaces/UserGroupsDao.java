package dao.interfaces;

import exceptions.DaoException;
import model.UserGroups;

import java.util.List;

public interface UserGroupsDao {
    List<UserGroups> getByLogin(String login) throws DaoException;
    List<UserGroups> getByIdIn(List<Integer> idList) throws DaoException;
    void insert(UserGroups userGroups) throws DaoException;
}
