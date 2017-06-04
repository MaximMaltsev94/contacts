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
import java.util.stream.Collectors;

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
    public List<ContactGroups> getByGroupIdIn(List<Integer> groupIdList) throws DaoException {
        return contactGroupsDao.getByGroupIdIn(groupIdList);
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
    public void deleteByGroupId(int groupId) throws DaoException {
        contactGroupsDao.deleteByGroupId(groupId);
    }

    private List<Integer> parseRequest(HttpServletRequest request, String prefix) {
        Enumeration<String> names = request.getAttributeNames();
        List<Integer> idList = new ArrayList<>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if(StringUtils.startsWith(name, prefix)) {
                int id = Integer.parseInt(StringUtils.substringAfter(name, "-"));
                idList.add(id);
            }
        }
        return idList;
    }

    @Override
    public List<ContactGroups> parseRequest(HttpServletRequest request, int contactId) {
        List<Integer> groupIdList = parseRequest(request, "contactGroup");
        return groupIdList.stream().map(id -> new ContactGroups(id, contactId)).collect(Collectors.toList());
    }

    @Override
    public List<ContactGroups> parseRequest(HttpServletRequest request, int groupId, boolean dummy) {
        List<Integer> contactIdList = parseRequest(request, "contact");
        return contactIdList.stream().map(id -> new ContactGroups(groupId, id)).collect(Collectors.toList());
    }
}
