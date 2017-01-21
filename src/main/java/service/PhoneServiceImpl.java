package service;

import dao.implementation.PhoneDaoImpl;
import dao.interfaces.PhoneDao;
import exceptions.DaoException;
import model.Phone;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PhoneServiceImpl implements PhoneService {
    private Connection connection;

    public PhoneServiceImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Phone> parseRequest(HttpServletRequest request, int contactId) {
        List<Phone> phoneList = new ArrayList<>();
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attrName = attributeNames.nextElement();
            if(!StringUtils.contains(attrName, "type_phone-")) {
                continue;
            }

            int i = Integer.parseInt(StringUtils.substringAfter(attrName, "type_phone-"));

            Phone phone = new Phone();
            phone.setContactID(contactId);
            phone.setType(request.getAttribute("type_phone-" + i).equals("1"));
            phone.setCountryID(Integer.parseInt((String) request.getAttribute("country_code_phone-" + i)) + 1);
            phone.setOperatorCode(Integer.parseInt((String)request.getAttribute("op_code_phone-" + i)));
            phone.setPhoneNumber(Long.parseLong((String)request.getAttribute("number_phone-" + i)));
            phone.setComment((String) request.getAttribute("comment_phone-" + i));

            phoneList.add(phone);
        }
        return phoneList;
    }

    @Override
    public void insert(Phone phone) throws DaoException{
        PhoneDao phoneDao = new PhoneDaoImpl(connection);
        phoneDao.insert(phone);
    }

    @Override
    public void insert(List<Phone> phoneList) throws DaoException {
        PhoneDao phoneDao = new PhoneDaoImpl(connection);
        phoneDao.insert(phoneList);
    }

    @Override
    public List<Phone> getByContactID(int contactID) throws DaoException {
        PhoneDao phoneDao = new PhoneDaoImpl(connection);
        return phoneDao.getPhoneByContactID(contactID);
    }

    @Override
    public void deleteByContactID(int contactID) throws DaoException {
        PhoneDao phoneDao = new PhoneDaoImpl(connection);
        phoneDao.deleteByContactID(contactID);
    }
}
