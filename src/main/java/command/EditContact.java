package command;

import dao.implementation.AttachmentDaoImpl;
import dao.implementation.ConnectionFactory;
import dao.implementation.ContactDaoImpl;
import dao.implementation.PhoneDaoImpl;
import dao.interfaces.AttachmentDao;
import dao.interfaces.ContactDao;
import dao.interfaces.PhoneDao;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Attachment;
import model.Contact;
import model.Phone;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactUtils;
import util.RequestUtils;
import util.TooltipType;

import javax.imageio.ImageIO;
import java.sql.SQLException;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditContact implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(EditContact.class);

    private List<String> savedFilesUrls = new ArrayList<>();
    private String savedImageUrl = "";

    private int parseContactID(List<FileItem> items) throws UnsupportedEncodingException {
        int contactID = 0;
        for (FileItem item : items) {
            if (item.isFormField()) {
                if (item.getFieldName().equals("id")) {
                    contactID = Integer.parseInt(ContactUtils.getUTF8String(item.getString()));
                }
            }
        }
        return contactID;
    }

    private void parseContactInfo(Contact contact, List<FileItem> items, String uploadPath) throws ParseException, UnsupportedEncodingException {
        for (FileItem item : items) {
            if (!item.isFormField()) {
                try {
                    //read image from input
                    BufferedImage image = ImageIO.read(item.getInputStream());
                    //check if loaded file is actually image
                    image.toString();

                    String profPicture = contact.getProfilePicture();
                    File fileToSave;
                    String imageName = ContactUtils.getFileNameFromUrl(profPicture, "pri");
                    if(imageName == null) {
                        fileToSave = File.createTempFile("pri", ".png", new File(uploadPath));
                    } else {
                        fileToSave = new File(uploadPath + imageName);
                    }
                    ImageIO.write(image, "png", fileToSave);

                    contact.setProfilePicture("?action=image&name=" + fileToSave.getName());
                    savedImageUrl = contact.getProfilePicture();

                } catch (Exception ex) {
                    LOG.warn("can't save profile image", ex);
                }
            } else {
                String itemValue = ContactUtils.getUTF8String(item.getString());
                if (StringUtils.isNotBlank(itemValue)) {
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
    }

    private List<Phone> parsePhones(List<FileItem> items, int contactID) throws UnsupportedEncodingException {
        List<Phone> phoneList = new ArrayList<>();
        for (FileItem item : items) {
            if (item.isFormField()) {
                String itemValue = ContactUtils.getUTF8String(item.getString());
                if (StringUtils.startsWith(item.getFieldName(), "type_phone-")) {
                    phoneList.add(new Phone());
                    phoneList.get(phoneList.size() - 1).setContactID(contactID);
                    phoneList.get(phoneList.size() - 1).setType(itemValue.equals("1"));
                } else if (StringUtils.startsWith(item.getFieldName(), "country_code_phone-")) {
                    phoneList.get(phoneList.size() - 1).setCountryID(Integer.parseInt(itemValue) + 1);
                } else if (StringUtils.startsWith(item.getFieldName(), "op_code_phone-")) {
                    phoneList.get(phoneList.size() - 1).setOperatorCode(Integer.parseInt(itemValue));
                } else if (StringUtils.startsWith(item.getFieldName(), "number_phone-")) {
                    phoneList.get(phoneList.size() - 1).setPhoneNumber(Integer.parseInt(itemValue));
                } else if (StringUtils.startsWith(item.getFieldName(), "comment_phone-")) {
                    phoneList.get(phoneList.size() - 1).setComment(itemValue);
                }
            }
        }
        return phoneList;
    }

    private List<Attachment> parseAttachments(List<FileItem> items, int contactID, String uploadPath) throws UnsupportedEncodingException, ParseException {
        List<Attachment> attachmentList = new ArrayList<>();
        for (FileItem item : items) {
            if (!item.isFormField()) {
                try {
                    if(StringUtils.startsWith(item.getFieldName(), "file_attachment")) {
                        //если ничего не пришло то не загружать
                        String fileExtension = StringUtils.substringAfter(item.getName(), ".");
                        File fileToSave = File.createTempFile("file", "." + fileExtension, new File(uploadPath));
                        item.write(fileToSave);
                        attachmentList.get(attachmentList.size() - 1).setFilePath("?action=document&name=" + fileToSave.getName());
                        savedFilesUrls.add(attachmentList.get(attachmentList.size() - 1).getFilePath());
                    }
                } catch (Exception e) {
                    LOG.warn("can't save file - {}", item.getName());
                }

            } else {
                String itemValue = ContactUtils.getUTF8String(item.getString());
                if (StringUtils.isNotBlank(itemValue)) {
                    if (StringUtils.startsWith(item.getFieldName(), "name_attachment")) {
                        attachmentList.add(new Attachment());
                        attachmentList.get(attachmentList.size() - 1).setContactID(contactID);
                        attachmentList.get(attachmentList.size() - 1).setFileName(itemValue);
                    } else if (StringUtils.startsWith(item.getFieldName(), "id_attachment")) {
                        attachmentList.get(attachmentList.size() - 1).setId(Integer.parseInt(itemValue));
                    } else if (StringUtils.startsWith(item.getFieldName(), "path_attachment")) {
                        attachmentList.get(attachmentList.size() - 1).setFilePath(itemValue);
                    } else if (StringUtils.startsWith(item.getFieldName(), "date_attachment")) {
                        Date uploadDate = DateUtils.parseDate(itemValue, "yyyy-MM-dd HH:mm:ss.S");
                        attachmentList.get(attachmentList.size() - 1).setUploadDate(uploadDate);
                    } else if (StringUtils.startsWith(item.getFieldName(), "comment_attachment")) {
                        attachmentList.get(attachmentList.size() - 1).setComment(itemValue);
                    }
                }
            }
        }
        return attachmentList;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandExecutionException, DataNotFoundException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            Connection connection = null;
            try {
                List<FileItem> items = ContactUtils.getMultipartItems(request, 10000);

                int contactID = parseContactID(items);

                connection = ConnectionFactory.getInstance().getConnection();

                ContactDao contactDao = new ContactDaoImpl(connection);
                Contact contact = contactDao.getByID(contactID);
                String uploadPath = request.getServletContext().getInitParameter("uploadPath");
                parseContactInfo(contact, items, uploadPath);
                List<Phone> phoneList = parsePhones(items, contactID);

                connection.setAutoCommit(false);
                contactDao.update(contact);

                PhoneDao phoneDao = new PhoneDaoImpl(connection);
                phoneDao.deleteByContactID(contactID);
                for (Phone phone : phoneList) {
                    phoneDao.insert(phone);
                }

                AttachmentDao attachmentDao = new AttachmentDaoImpl(connection);
                List<Attachment> attachmentList = attachmentDao.getByContactId(contactID);
                List<Attachment> postedAttachments = parseAttachments(items, contactID, uploadPath);

                List<Attachment> forDelete = new ArrayList<>();
                List<Attachment> forUpdate = new ArrayList<>();
                for (Attachment attachment : attachmentList) {
                    boolean flag = true;
                    for (Attachment postedAttachment : postedAttachments) {
                        if(postedAttachment.getId() == attachment.getId()) {
                            attachment.setFileName(postedAttachment.getFileName());
                            attachment.setComment(postedAttachment.getComment());
                            flag = false;
                            break;
                        }
                    }
                    if(flag)
                        forDelete.add(attachment);
                    else
                        forUpdate.add(attachment);
                }

                for (Attachment attachment : forDelete) {
                    attachmentDao.delete(attachment);
                    boolean deleteResult = ContactUtils.deleteFileByUrl(attachment.getFilePath(), uploadPath, "file");
                    if(deleteResult == false) {
                        LOG.warn("can't delete file - {}", attachment.getFilePath());
                    }
                }

                for (Attachment attachment : forUpdate) {
                    attachmentDao.update(attachment);
                }

                for (Attachment postedAttachment : postedAttachments) {
                    if(postedAttachment.getId() == 0) {
                        attachmentDao.insert(postedAttachment);
                    }
                }

                connection.commit();
                connection.setAutoCommit(true);
                RequestUtils.setMessageText(request, "Контакт " + contact.getFirstName() + " " + contact.getLastName() + " успешно редактирован", TooltipType.success);
            } catch (SQLException | DaoException e) {
                LOG.warn("edit transaction error");
                String uploadPath = request.getServletContext().getInitParameter("uploadPath");

                LOG.info("deleting profile image - {}", savedImageUrl);
                ContactUtils.deleteFileByUrl(savedImageUrl, uploadPath, "pri");

                for (String savedFileUrl : savedFilesUrls) {
                    LOG.info("deleting uploaded attachment - {}", savedFileUrl);
                    ContactUtils.deleteFileByUrl(savedFileUrl, uploadPath, "file");
                }
                try {
                    if (connection != null) {
                        LOG.info("rolling back transaction ", e);
                        connection.rollback();
                    }
                } catch (SQLException e1) {
                    LOG.warn("error while rolling back transaction ", e1);
                }
                RequestUtils.setMessageText(request, "Произошла ошибка при редактировании. Информация контакта не обновлена", TooltipType.danger);
            } catch (ConnectionException e) {
                LOG.error("can't get connection to database", e);
                throw new CommandExecutionException("error while connecting to database", e);
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
                try {
                    if (connection != null)
                        connection.close();
                } catch (SQLException e) {
                    LOG.warn("error while closing connection ", e);
                }
            }
        }

        return null;
    }
}
