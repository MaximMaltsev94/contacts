package command;
import dao.interfaces.ContactDao;
import dao.mysqlimplementation.MySqlConnectionFactory;
import dao.mysqlimplementation.MySqlContactDao;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.FrontController;
import util.ContactUtils;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(EmailHandler.class);
    private void processRequest(HttpServletRequest request, HttpServletResponse response, List<Integer> contactIds) throws ServletException, IOException {
        try(Connection connection = MySqlConnectionFactory.getInstance().getConnection()) {
            ContactDao contactDao = new MySqlContactDao(connection);
            List<Contact> contactsWithEmail = contactDao.getContactsWithEmail();

            request.setAttribute("contactList", contactsWithEmail);
            request.setAttribute("selectedContacts", contactIds);

            Map<String, String> templates = readTemplates();
            request.setAttribute("templates", templates);

            request.getRequestDispatcher("/WEB-INF/view/email.jsp").forward(request, response);
        } catch (SQLException | NamingException e) {
            LOG.warn("can't get db connection", e);
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
            LOG.warn("can't find resource folder", e);
        }
        return templates;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitedIDs = request.getParameter("id").split(",");
        List<Integer> contactIds = new ArrayList<>();
        for (String id : splitedIDs) {
            contactIds.add(Integer.parseInt(id));
        }
        processRequest(request, response, contactIds);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, new ArrayList<>());
    }
}
