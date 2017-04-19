package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import model.ContactGroups;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ContactGroupsService;
import service.ContactGroupsServiceImpl;
import service.ContactService;
import service.ContactServiceImpl;
import util.EmailHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GetEmailPage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetEmailPage.class);
    private EmailHelper emailHelper = new EmailHelper();

    private Map<String, String> readTemplates() {
        Map<String, String> templates = new TreeMap<>();
        Pattern p = Pattern.compile("^template.*$");

        try {
            File[] fileList = new File(getClass().getResource("../").toURI())
                    .listFiles((dir, name) -> p.matcher(name).matches());

            if(fileList != null) {
                templates = Arrays.stream(fileList)
                        .map(emailHelper::readTemplateFile)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
        } catch (URISyntaxException e) {
            LOG.error("can't find resource folder", e);
        }
        return templates;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_NAME = "email";

        String idAttribute = (String) request.getAttribute("id");


        List<Integer> contactIdList = null;
        if(idAttribute != null) {
            contactIdList = Arrays.stream(idAttribute.split(","))
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());
        }

        if(contactIdList == null) {
            contactIdList = Collections.emptyList();
        }

        String groupIdAttribute = (String) request.getAttribute("groupId");
        List<Integer> groupIdList = null;
        if(groupIdAttribute != null) {
            groupIdList = Arrays.stream(groupIdAttribute.split(","))
                    .map(id -> Integer.parseInt(StringUtils.substringAfter(id,"manage-group-")))
                    .collect(Collectors.toList());
        }

        if(groupIdList == null) {
            groupIdList = Collections.emptyList();
        }

        try {
            ContactService contactService = new ContactServiceImpl(connection);
            ContactGroupsService contactGroupsService = new ContactGroupsServiceImpl(connection);

            List<Contact> contactsWithEmail = contactService.getByEmailNotNullAndLoginUser(request.getUserPrincipal().getName());
            List<ContactGroups> contactGroupsList = contactGroupsService.getByGroupIdIn(groupIdList);
            List<Integer> contactGroupsContactIdList = contactGroupsList.stream().map(ContactGroups::getContactID).collect(Collectors.toList());;

            List<Integer> selectedContacts = new ArrayList<>();
            selectedContacts.addAll(contactIdList);
            selectedContacts.addAll(contactGroupsContactIdList);


            request.setAttribute("contactList", contactsWithEmail);
            request.setAttribute("selectedContacts", selectedContacts);

            Map<String, String> templates = readTemplates();
            request.setAttribute("templates", templates);

        }  catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        }

        return VIEW_NAME;
    }
}
