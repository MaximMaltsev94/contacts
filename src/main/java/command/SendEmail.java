package command;

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
import util.EmailHelper;
import util.RequestUtils;
import util.TooltipType;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SendEmail implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(SendEmail.class);

    private String processMessage(Contact contact, Relationship relationship, Country country, City city, String message) {
        ST template = new ST(message);
        template.add("firstName", contact.getFirstName());
        template.add("lastName", contact.getLastName());
        if(contact.getPatronymic() != null)
            template.add("patronymic", contact.getPatronymic());
        if(contact.getBirthDate() != null) {
            template.add("birthDate", DateFormatUtils.format(contact.getBirthDate(), "dd MMMM yyyy"));
        }
        String gender = "жен.";
        if(contact.getGender() == true) {
            gender = "муж.";
        }
        template.add("gender", gender);
        template.add("citizenship", contact.getCitizenship());
        if(relationship != null)
            template.add("relationship", relationship.getName());

        template.add("website", contact.getWebSite());
        template.add("companyName", contact.getCompanyName());

        if(country != null)
            template.add("country", country.getName());

        if(city != null)
            template.add("city", city.getName());

        template.add("street", contact.getStreet());
        template.add("postcode", contact.getPostcode());
        return template.render();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        ContactService contactService = new ContactServiceImpl(connection);
        RelationshipService relationshipService = new RelationshipServiceImpl(connection);
        CountryService countryService = new CountryServiceImpl(connection);
        CityService cityService = new CityServiceImpl(connection);

        boolean isErrorOccurred = true;
        try {

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


            EmailHelper emailHelper = new EmailHelper();
            String emailSubject = (String) request.getAttribute("subject");
            String emailText = (String) request.getAttribute("text");
            for (Contact contact : contactList) {

                Relationship relationship = relationshipMap.get(contact.getRelationshipID());
                Country country = countryMap.get(contact.getCountryID());
                City city = cityMap.get(contact.getCityID());

                String message = processMessage(contact, relationship, country, city, emailText);

                emailHelper.sendEmail(contact.getEmail(), emailSubject, message);
            }
            isErrorOccurred = false;
        }  catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        } catch (MessagingException e) {
            LOG.error("can't send email to address", e);
        }

        if(isErrorOccurred) {
            RequestUtils.setMessageText(request, "Произошла ошибка при отправке письма", TooltipType.danger);
        } else {
            RequestUtils.setMessageText(request, "Письмо успешно отправлено", TooltipType.success);
        }

        return null;
    }
}
