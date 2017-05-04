package dao.interfaces;

import exceptions.DaoException;
import model.UserGroups;

import java.util.List;

public interface UserGroupsDao {
    List<UserGroups> getByLogin(String login) throws DaoException;
    UserGroups getByIdAndLoginUser(int id, String loginUser) throws DaoException;
    List<UserGroups> getByIdIn(List<Integer> idList) throws DaoException;
    UserGroups insert(UserGroups userGroups) throws DaoException;
    void update(UserGroups userGroups) throws DaoException;
    void delete(List<Integer> idList) throws DaoException;

    List<UserGroups> getByGroupNameAndLoginUser(String groupName, String loginUser) throws DaoException;
}
