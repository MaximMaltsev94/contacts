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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class EditHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(EditHandler.class);

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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if(isMultipart) {
            try {
                DiskFileItemFactory factory = initDiskFactory(request, 10000);

                // Create a new file upload handler
                ServletFileUpload upload = new ServletFileUpload(factory);

                // Parse the request
                List<FileItem> items = upload.parseRequest(request);

                // Process the uploaded items
                Iterator<FileItem> iter = items.iterator();

                int contactID = 0;
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    if(item.isFormField()) {
                        if(item.getFieldName().equals("id")) {
                            contactID = Integer.parseInt(new String(item.getString().getBytes("iso-8859-1"), "UTF-8"));
                        }
                    }
                }

                ContactDao contactDao = new MySqlContactDao();
                Contact contact = contactDao.getByID(contactID);

                iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    if (!item.isFormField()) {
                        try {
                            //read image from input
                            BufferedImage image = ImageIO.read(item.getInputStream());
                            //check if loaded file is actually image
                            image.toString();

                            String uploadPath = request.getServletContext().getInitParameter("uploadPath");
                            String profPicture = contact.getProfilePicture();
                            File fileToSave;
                            if(profPicture.contains("pri")) {
                                String imageName = profPicture.substring(profPicture.indexOf("pri"));
                                fileToSave = new File(uploadPath + imageName);
                            } else {
                                fileToSave = File.createTempFile("pri", ".png", new File(uploadPath));
                            }
                            ImageIO.write(image, "png", fileToSave);

                            contact.setProfilePicture("/contact/?action=image&name=" + fileToSave.getName());

                        } catch (Exception ex) {
                            LOG.warn("can't save profile image", ex);
                        }
                    } else {
                        String itemValue = new String(item.getString().getBytes("iso-8859-1"), "UTF-8");
                        if(StringUtils.isNotBlank(itemValue)) {
                            switch (item.getFieldName()) {
                                case "id":
                                    contact.setId(Integer.parseInt(itemValue));
                                    break;
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

                contactDao.update(contact);
                response.sendRedirect("/contact/?action=show&page=1");
            } catch (Exception ex) {
                LOG.warn("can't add contact", ex);
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ContactDao contactDao = new MySqlContactDao();
            int contactID = Integer.parseInt(request.getParameter("id"));
            int maxID = contactDao.getMaxID();
            if(contactID < 1 || contactID > maxID) {
                throw new NumberFormatException("id out of range");
            }

            Contact contact = contactDao.getByID(contactID);
            request.setAttribute("contact", contact);

            RelationshipDao rshDao = new MySqlRelationshipDao();
            List<Relationship> relationshipList = rshDao.getAll();
            request.setAttribute("relationshipList", relationshipList);

            CountryDao countryDao = new MySqlCountryDao();
            List<Country> countryList = countryDao.getAll();
            request.setAttribute("countryList", countryList);

            CityDao cityDao = new MySqlCityDao();
            List<City> cityList = cityDao.getAll();
            request.setAttribute("cityList", cityList);

            PhoneDao phoneDao = new MySqlPhoneDao();
            List<Phone> phoneList = phoneDao.getPhoneByContactID(contactID);
            request.setAttribute("phoneList", phoneList);

            request.getRequestDispatcher("/WEB-INF/view/editContact.jsp").forward(request, response);
        } catch (NumberFormatException ex) {
            LOG.warn("incorrect contact id {}", request.getParameter("id"), ex);
            response.sendRedirect("/contact/?action=show&page=1");
        }catch (ServletException | IOException e) {
            LOG.warn("can't forward request - {}", "/WEB-INF/view/editContact.jsp", e);
        } catch (NamingException e) {
            LOG.warn("can't get db connection", e);
        }
    }
}
