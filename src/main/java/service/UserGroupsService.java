package service;

import exceptions.DaoException;
import model.UserGroups;

import java.util.List;

public interface UserGroupsService {
    List<UserGroups> getByLogin(String login) throws DaoException;
    List<UserGroups> getByIdIn(List<Integer> idList) throws DaoException;
    void insert(UserGroups userGroups) throws DaoException;
}
