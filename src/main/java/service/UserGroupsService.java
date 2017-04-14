package service;

import exceptions.DaoException;
import model.UserGroups;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserGroupsService {
    List<UserGroups> getByLogin(String login) throws DaoException;
    UserGroups getByIdAndLoginUser(int id, String loginUser) throws DaoException;
    List<UserGroups> getByIdIn(List<Integer> idList) throws DaoException;
    UserGroups insert(UserGroups userGroups) throws DaoException;
    void update(UserGroups userGroups) throws DaoException;
}
