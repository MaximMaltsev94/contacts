package service;

import dao.implementation.ContactGroupsDaoImpl;
import dao.interfaces.ContactGroupsDao;
import exceptions.DaoException;
import model.ContactGroups;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
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

    @Override
    public List<ContactGroups> getByContactId(int contactId) throws DaoException {
        return contactGroupsDao.getByContactId(contactId);
    }

    @Override
    public List<ContactGroups> getByContactIdIn(List<Integer> contactIdList) throws DaoException {
        return contactGroupsDao.getByContactIdIn(contactIdList);
    }

    @Override
    public void insert(List<ContactGroups> contactGroupsList) throws DaoException {
        contactGroupsDao.insert(contactGroupsList);
    }

    @Override
    public void deleteByContactId(int contactId) throws DaoException {
        contactGroupsDao.deleteByContactId(contactId);
    }

    @Override
    public List<ContactGroups> parseRequest(HttpServletRequest request, int contactId) {
        Enumeration<String> names = request.getAttributeNames();
        List<ContactGroups> groupIdList = new ArrayList<>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if(StringUtils.startsWith(name, "contactGroup")) {
                int groupId = Integer.parseInt(StringUtils.substringAfter(name, "-"));
                groupIdList.add(new ContactGroups(groupId, contactId));
            }
        }
        return groupIdList;
    }
}
