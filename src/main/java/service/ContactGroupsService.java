package service;

import exceptions.DaoException;
import model.ContactGroups;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ContactGroupsService {
    List<ContactGroups> getByGroupId(int groupId) throws DaoException;
    List<ContactGroups> getByGroupIdIn(List<Integer> groupIdList) throws DaoException;
    List<ContactGroups> getByContactId(int contactId) throws DaoException;
    List<ContactGroups> getByContactIdIn(List<Integer> contactIdList) throws DaoException;
    void insert(List<ContactGroups> contactGroupsList) throws DaoException;
    void deleteByContactId(int contactId) throws DaoException;
    void deleteByGroupId(int groupId) throws DaoException;

    List<ContactGroups> parseRequest(HttpServletRequest request, int contactId);
    List<ContactGroups> parseRequest(HttpServletRequest request, int groupId, boolean dummy);
}
