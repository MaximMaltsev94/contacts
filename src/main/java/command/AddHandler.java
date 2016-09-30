package command;

import dao.interfaces.*;
import dao.mysqlimplementation.*;
import model.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by maxim on 19.09.2016.
 */
public class AddHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(AddHandler.class);

    private DiskFileItemFactory initDiskFactory(HttpServletRequest request, int maxFileSize) {
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = request.getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        factory.setSizeThreshold(maxFileSize);
        return factory;
    }

    private Contact parseContactInfo(List<FileItem> items, String uploadPath) throws UnsupportedEncodingException, ParseException {
        Contact contact = new Contact();
        contact.setProfilePicture("/sysImages/default.png");
        for(FileItem item: items) {
            if (!item.isFormField()) {
                try {
                    //read image from input
                    BufferedImage image = ImageIO.read(item.getInputStream());
                    //check if loaded file is actually image
                    image.toString();

                    File fileToSave = File.createTempFile("pri", ".png", new File(uploadPath));
                    ImageIO.write(image, "png", fileToSave);

                    contact.setProfilePicture("/contact/?action=image&name=" + fileToSave.getName());

                } catch (Exception ex) {
                    LOG.warn("can't save profile image", ex);
                }
            } else {
                String itemValue = new String(item.getString().getBytes("iso-8859-1"), "UTF-8");
                if(StringUtils.isNotBlank(itemValue)) {
                    switch (item.getFieldName()) {
                        case "firstName":
                            contact.setFirstName(itemValue);
                            break;
                        case "lastName":
                            contact.setLastName(itemValue);
                            break;
                        case "patronymic":
                            contact.setPatronymic(itemValue);
                            break;
                        case "birthDate":
                            Date birthDate =  DateUtils.parseDate(itemValue, "yyyy-MM-dd");
                            contact.setBirthDate(birthDate);
                            break;
                        case "gender":
                            contact.setGender(itemValue.charAt(0) == '1');
                            break;
                        case "citizenship":
                            contact.setCitizenship(itemValue);
                            break;
                        case "relationship":
                            contact.setRelationshipID(Integer.parseInt(itemValue));
                            break;
                        case "webSite":
                            contact.setWebSite(itemValue);
                            break;
                        case "email":
                            contact.setEmail(itemValue);
                            break;
                        case "companyName":
                            contact.setCompanyName(itemValue);
                            break;
                        case "country":
                            contact.setCountryID(Integer.parseInt(itemValue));
                            break;
                        case "city":
                            contact.setCityID(Integer.parseInt(itemValue));
                            break;
                        case "street":
                            contact.setStreet(itemValue);
                            break;
                        case "postcode":
                            contact.setPostcode(itemValue);
                            break;
                    }
                }
            }
        }
        return contact;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if(isMultipart) {
            Connection connection = null;
            try {
                DiskFileItemFactory factory = initDiskFactory(request, 10000);
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(request);

                Contact contact = parseContactInfo(items, request.getServletContext().getInitParameter("uploadPath"));

                connection = MySqlConnectionFactory.getInstance().getConnection();
                ContactDao contactDao = new MySqlContactDao(connection);
                contactDao.insert(contact);
                response.sendRedirect("/contact/?action=show&page=" + request.getSession().getAttribute("lastVisitedPage"));
            } catch (Exception ex) {
                LOG.warn("can't add contact", ex);
            } finally {
                if(connection != null)
                    try {
                        connection.close();
                    } catch (SQLException e) {}
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;
        try {
            connection = MySqlConnectionFactory.getInstance().getConnection();
            RelationshipDao rshDao = new MySqlRelationshipDao(connection);
            List<Relationship> relationshipList = rshDao.getAll();
            request.setAttribute("relationshipList", relationshipList);

            CountryDao countryDao = new MySqlCountryDao(connection);
            List<Country> countryList = countryDao.getAll();
            request.setAttribute("countryList", countryList);

            CityDao cityDao = new MySqlCityDao(connection);
            List<City> cityList = cityDao.getAll();
            request.setAttribute("cityList", cityList);
            request.getRequestDispatcher("/WEB-INF/view/addContact.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            LOG.warn("can't forward request - {}", "/WEB-INF/view/addContact.jsp", e);
        } catch (NamingException | SQLException e) {
            LOG.warn("can't get db connection", e);
        } finally {
            if(connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {}
        }
    }
}