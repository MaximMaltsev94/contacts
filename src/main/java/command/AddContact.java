package command;

import dao.implementation.ConnectionFactory;
import dao.implementation.ContactDaoImpl;
import dao.interfaces.ContactDao;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactUtils;
import util.TooltipType;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class AddContact implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(AddContact.class);

    private String savedImageUrl = "";

    private Contact parseContactInfo(List<FileItem> items, String uploadPath) throws UnsupportedEncodingException, ParseException, FileUploadException {
        Contact contact = new Contact();
        contact.setProfilePicture("/sysImages/default.png");
        for (FileItem item : items) {
            if (!item.isFormField()) {
                try {
                    //read image from input
                    BufferedImage image = ImageIO.read(item.getInputStream());
                    if(image == null) {
                        LOG.info("profile image not specified");
                        continue;
                    }
                    //check if loaded file is actually image
                    image.toString();

                    File fileToSave = File.createTempFile("pri", ".png", new File(uploadPath));
                    ImageIO.write(image, "png", fileToSave);

                    contact.setProfilePicture("?action=image&name=" + fileToSave.getName());
                    savedImageUrl = contact.getProfilePicture();

                } catch (Exception e) {
                    LOG.error("can't save profile image", e);
                }
            } else {
                String itemValue = ContactUtils.getUTF8String(item.getString());
                if (StringUtils.isNotBlank(itemValue)) {
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
                            Date birthDate = DateUtils.parseDate(itemValue, "dd.MM.yyyy");
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

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandExecutionException, DataNotFoundException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        Contact contact = null;
        boolean isErrorOccurred = true;
        if (isMultipart) {
            try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
                List<FileItem> items = ContactUtils.getMultipartItems(request, 10000);

                contact = parseContactInfo(items, request.getServletContext().getInitParameter("uploadPath"));

                ContactDao contactDao = new ContactDaoImpl(connection);
                contactDao.insert(contact);
                request.getSession().setAttribute("tooltip-type", TooltipType.success.toString());
                request.getSession().setAttribute("tooltip-text", "Контакт " + contact.getFirstName() + " " + contact.getLastName() + " успешно сохранен");
                isErrorOccurred = false;
            } catch (DaoException e) {
                LOG.error("can't insert contact - {} to database", contact, e);
                throw new CommandExecutionException("error while inserting contacts to database", e);
            } catch (ConnectionException e) {
                LOG.error("can't get connection to database", e);
                throw new CommandExecutionException("error while connecting to database", e);
            } catch (SQLException e) {
                LOG.error("can't close connection to database", e);
                throw new CommandExecutionException("error while closing database connection", e);
            } catch (FileUploadException e) {
                LOG.error("can't save profile image", e);
                throw new CommandExecutionException("error while saving profile image", e);
            } catch (UnsupportedEncodingException e) {
                LOG.error("some problems with parameters encoding", e);
                throw new CommandExecutionException("error while process parameter's encoding", e);
            } catch (ParseException e) {
                // TODO: 14.01.2017 change date when params will be in request attributes
                LOG.error("can't parse date - {}", "!!!", e);
                throw new CommandExecutionException("error while parsing contact's birth date", e);
            } finally {
                if(isErrorOccurred) {
                    LOG.info("deleting profile image from file system - {}", savedImageUrl);
                    ContactUtils.deleteFileByUrl(savedImageUrl, request.getServletContext().getInitParameter("uploadPath"), "pri");
                }
            }
        }

        return null;
    }
}
