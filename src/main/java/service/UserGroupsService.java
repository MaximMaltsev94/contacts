package service;

import exceptions.DaoException;
import model.UserGroups;

public interface UserGroupsService {
    void insert(UserGroups userGroups) throws DaoException;
}
