package command;

import dao.implementation.ConnectionFactory;
import dao.implementation.ContactDaoImpl;
import dao.interfaces.ContactDao;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetEmailPage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetEmailPage.class);

    private void processRequest(HttpServletRequest request, HttpServletResponse response, List<Integer> contactIds) throws CommandExecutionException{
        try(Connection connection = ConnectionFactory.getInstance().getConnection()) {
            ContactDao contactDao = new ContactDaoImpl(connection);
            List<Contact> contactsWithEmail = contactDao.getContactsWithEmail();

            request.setAttribute("contactList", contactsWithEmail);
            request.setAttribute("selectedContacts", contactIds);

            Map<String, String> templates = readTemplates();
            request.setAttribute("templates", templates);

        }  catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        } catch (ConnectionException e) {
            LOG.error("can't get connection to database", e);
            throw new CommandExecutionException("error while connecting to database", e);
        } catch (SQLException e) {
            LOG.error("can't close connection to database", e);
            throw new CommandExecutionException("error while closing database connection", e);
        }
    }

    private Map<String, String> readTemplates() {
        Map<String, String> templates = new TreeMap<>();
        try {
            File[] fileList = new File(getClass().getResource("../").toURI()).listFiles((dir, name) -> {
                Pattern p = Pattern.compile("^template.*$");
                Matcher m = p.matcher(name);
                return m.matches();
            });

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
        } catch (URISyntaxException e) {
            LOG.error("can't find resource folder", e);
        }
        return templates;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_NAME = "email";

        String idListString = request.getParameter("id");

        List<Integer> contactIds = new ArrayList<>();
        if(idListString != null) {
            String[] splitedIDs = idListString.split(",");
            for (String id : splitedIDs) {
                contactIds.add(Integer.parseInt(id));
            }
        }
        processRequest(request, response, contactIds);

        return VIEW_NAME;
    }
}
