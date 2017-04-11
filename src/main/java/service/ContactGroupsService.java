package service;

import exceptions.DaoException;
import model.ContactGroups;

import java.util.List;

public interface ContactGroupsService {
    List<ContactGroups> getByGroupId(int groupId) throws DaoException;
}
