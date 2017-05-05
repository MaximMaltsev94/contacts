package command;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.City;
import model.Contact;
import model.Country;
import model.Relationship;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import service.*;
import sun.rmi.runtime.Log;
import util.ContactUtils;
import util.EmailHelper;
import util.RequestUtils;
import util.TooltipType;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SendEmail implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(SendEmail.class);
    private EmailHelper emailHelper = new EmailHelper();

    private Map<String, Object> getTemplateArgsMap(Contact contact, Relationship relationship, Country country, City city) {
        Map<String, Object> map = new HashMap<>();

        map.put("firstName", contact.getFirstName());
        map.put("lastName", contact.getLastName());
        if(contact.getPatronymic() != null)
            map.put("patronymic", contact.getPatronymic());
        map.put("birthDate", contact.getBirthDay() + " " + contact.getBirthMonth() + " " + contact.getBirthYear());
        String gender = "не. указ.";
        if(contact.getGender() == ContactUtils.GENDER_MAN) {
            gender = "муж.";
        } else if(contact.getGender() == ContactUtils.GENDER_WOMAN) {
            gender = "жен.";
        }
        map.put("gender", gender);
        map.put("citizenship", contact.getCitizenship());
        if(relationship != null)
            map.put("relationship", relationship.getName());

        map.put("website", contact.getWebSite());
        map.put("companyName", contact.getCompanyName());

        if(country != null)
            map.put("country", country.getName());

        if(city != null)
            map.put("city", city.getName());

        map.put("street", contact.getStreet());
        map.put("postcode", contact.getPostcode());

        return map;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        ContactService contactService = new ContactServiceImpl(connection);
        RelationshipService relationshipService = new RelationshipServiceImpl(connection);
        CountryService countryService = new CountryServiceImpl(connection);

        boolean isErrorOccurred = true;
        try {
            CityService cityService = new CityServiceVkImpl(new VKServiceImpl(null));

            List<Integer> idList = Arrays.stream(((String) request.getAttribute("id")).split(","))
                                            .map(Integer::parseInt)
                                            .collect(Collectors.toList());


            List<Contact> contactList = contactService.getByIdInAndLoginUser(idList, request.getUserPrincipal().getName());

            List<Integer> countryIdList = contactList.stream().map(Contact::getCountryID).collect(Collectors.toList());
            List<Country> countryList = countryService.getByIDIn(countryIdList);
            Map<Integer, Country> countryMap = countryList.stream().collect(Collectors.toMap(Country::getId, country -> country));

            List<Integer> cityIdList = contactList.stream().map(Contact::getCityID).collect(Collectors.toList());
            List<City> cityList = cityService.getByIDIn(cityIdList);
            Map<Integer, City> cityMap = cityList.stream().collect(Collectors.toMap(City::getId, city -> city));

            List<Relationship> relationshipList = relationshipService.getAll();
            Map<Integer, Relationship> relationshipMap = relationshipList.stream().collect(Collectors.toMap(Relationship::getId, relationship -> relationship));


            String emailSubject = (String) request.getAttribute("subject");
            String emailText = (String) request.getAttribute("text");
            for (Contact contact : contactList) {

                Relationship relationship = relationshipMap.get(contact.getRelationshipID());
                Country country = countryMap.get(contact.getCountryID());
                City city = cityMap.get(contact.getCityID());

                Map<String, Object> args = getTemplateArgsMap(contact, relationship, country, city);

                String message = emailHelper.processTemplate(emailText, args);
                message = message.replaceAll(System.lineSeparator(), "<br>");

                emailHelper.sendEmail(contact.getEmail(), emailSubject, message);
            }
            isErrorOccurred = false;
        }  catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        } catch (MessagingException e) {
            LOG.error("can't send email to address", e);
        } catch (IOException e) {
            LOG.error("can't read vk properties file", e);
            throw new CommandExecutionException("error while reading vk properties file", e);
        } catch (ApiException | ClientException e) {
            LOG.error("can't access vk api", e);
            throw new CommandExecutionException("error while accesing vk api", e);
        }

        if(isErrorOccurred) {
            RequestUtils.setMessageText(request, "Произошла ошибка при отправке письма", TooltipType.danger);
        } else {
            RequestUtils.setMessageText(request, "Письмо успешно отправлено", TooltipType.success);
        }

        return null;
    }
}
