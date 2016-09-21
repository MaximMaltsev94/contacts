package command;

import dao.interfaces.*;
import dao.mysqlimplementation.*;
import model.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.imageio.ImageIO;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

/**
 * Created by maxim on 19.09.2016.
 */
public class AddHandler implements RequestHandler {

    private void doPost(HttpServletRequest request, HttpServletResponse response) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if(isMultipart) {
            try {
                // Create a factory for disk-based file items
                DiskFileItemFactory factory = new DiskFileItemFactory();

                // Configure a repository (to ensure a secure temp location is used)
                ServletContext servletContext = request.getServletContext();
                File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
                factory.setRepository(repository);

                // Create a new file upload handler
                ServletFileUpload upload = new ServletFileUpload(factory);

                // Parse the request
                List<FileItem> items = upload.parseRequest(request);

                // Process the uploaded items
                Iterator<FileItem> iter = items.iterator();

                Contact contact = new Contact();
                contact.setProfilePicture("/sysImages/default.png");
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    if (!item.isFormField()) {
                        try {
                            // Check if loaded file is actually image
                            ImageIO.read(item.getInputStream()).toString();

                            String uploadPath = request.getServletContext().getInitParameter("uploadPath");
                            String fileExtension = item.getName().substring(item.getName().indexOf('.'));
                            File fileToSave = File.createTempFile("pri", fileExtension, new File(uploadPath));
                            item.write(fileToSave);

                            contact.setProfilePicture("/contact/?action=image&name=" + fileToSave.getName());

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        String itemValue = new String(item.getString().getBytes("iso-8859-1"), "UTF-8");
                        if(item.getString().length() != 0) {
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
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD");
                                    java.util.Date d = format.parse(itemValue);

                                    contact.setBirthDate(d);
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
                                    contact.setCountyID(Integer.parseInt(itemValue));
                                    break;
                                case "city":
                                    contact.setCityID(Integer.parseInt(itemValue));
                                    break;
                                case "street":
                                    contact.setStreet(itemValue);
                                    break;
                                case "postcode":
                                    contact.setStreet(itemValue);
                                    break;
                            }
                        }
                    }
                }

                ContactDao contactDao = new MySqlContactDao();
                contactDao.insert(contact);
                response.sendRedirect("/contact/?action=show");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getMethod().equalsIgnoreCase("get")) {
            try {
                RelationshipDao rshDao = new MySqlRelationshipDao();
                List<Relationship> relationshipList = rshDao.getAll();
                request.setAttribute("relationshipList", relationshipList);

                CountryDao countryDao = new MySqlCountryDao();
                List<Country> countryList = countryDao.getAll();
                request.setAttribute("countryList", countryList);

                CityDao cityDao = new MySqlCityDao();
                List<City> cityList = cityDao.getAll();
                request.setAttribute("cityList", cityList);
                request.getRequestDispatcher("/WEB-INF/view/addContact.jsp").forward(request, response);
            } catch (NamingException e) {
                e.printStackTrace();
            }
        } else if(request.getMethod().equalsIgnoreCase("post")) {
            doPost(request, response);
        }
    }
}
