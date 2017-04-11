package service;

import dao.implementation.ContactGroupsDaoImpl;
import dao.interfaces.ContactGroupsDao;
import exceptions.DaoException;
import model.ContactGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class ContactGroupsServiceImpl implements ContactGroupsService {
    private static final Logger LOG = LoggerFactory.getLogger(ContactGroupsServiceImpl.class);
    private ContactGroupsDao contactGroupsDao;

    public ContactGroupsServiceImpl(Connection connection) {
        this.contactGroupsDao = new ContactGroupsDaoImpl(connection);
    }

    @Override
    public List<ContactGroups> getByGroupId(int groupId) throws DaoException {
        return contactGroupsDao.getByGroupId(groupId);
    }
}
