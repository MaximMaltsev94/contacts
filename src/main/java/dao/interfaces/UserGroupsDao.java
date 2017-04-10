package dao.interfaces;

import exceptions.DaoException;
import model.UserGroups;

public interface UserGroupsDao {
    void insert(UserGroups userGroups) throws DaoException;
}
