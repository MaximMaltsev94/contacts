package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ContactService;
import service.ContactServiceImpl;

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

    private Map<String, String> readTemplates() {
        Map<String, String> templates = new TreeMap<>();
        Pattern p = Pattern.compile("^template.*$");

        try {
            File[] fileList = new File(getClass().getResource("../").toURI())
                    .listFiles((dir, name) -> p.matcher(name).matches());

            if(fileList != null) {
                for (File file : fileList) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
                        String templateName = reader.readLine();
                        StringBuilder templateBody = new StringBuilder();
                        String str;
                        while ((str = reader.readLine()) != null) {
                            templateBody.append(str);
                            templateBody.append(System.getProperty("line.separator"));
                        }
                        templates.put(templateName, templateBody.toString());
                    } catch (IOException e) {
                        LOG.warn("can't read template file - {}", file.getName(), e);
                    }
                }
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
            contactIdList = new ArrayList<>();
        }

        try {
            ContactService contactService = new ContactServiceImpl(connection);
            List<Contact> contactsWithEmail = contactService.getByEmailNotNullAndLoginUser(request.getUserPrincipal().getName());

            request.setAttribute("contactList", contactsWithEmail);
            request.setAttribute("selectedContacts", contactIdList);

            Map<String, String> templates = readTemplates();
            request.setAttribute("templates", templates);

        }  catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        }

        return VIEW_NAME;
    }
}
