package command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import model.ContactGroups;
import model.Country;
import model.UserGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;
import util.ContactUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetUserPage implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(GetUserPage.class);

    private int[] calcAgeStatistics(List<Contact> contacts, int size) {
        int currentYear = LocalDate.now().getYear();
        int [] count = new int[size];
        Arrays.fill(count, 0);
        for (Contact contact : contacts) {
            int age = currentYear - contact.getBirthYear();
            if(age < 18) {
                count[0]++;
            } else if(age >= 18 && age < 21) {
                count[1]++;
            } else if(age >= 21 && age < 24) {
                count[2]++;
            } else if(age >= 24 && age < 27) {
                count[3]++;
            } else if(age >= 27 && age < 30) {
                count[4]++;
            } else if(age >= 30 && age < 35) {
                count[5]++;
            } else if(age >= 35 && age < 45) {
                count[6]++;
            } else if(age >= 45) {
                count[7]++;
            }
        }
        return count;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        CountryService countryService = new CountryServiceImpl(connection);
        ContactService contactService = new ContactServiceImpl(connection);
        UserGroupsService userGroupsService = new UserGroupsServiceImpl(connection);
        ContactGroupsService contactGroupsService = new ContactGroupsServiceImpl(connection);
        ObjectMapper mapper = new ObjectMapper();

        try {
            String loginUser = request.getUserPrincipal().getName();
            List<Contact> contactList = contactService.getByLoginUser(loginUser);
            Map<Integer, Long> contactCountByCountry = contactList.stream().collect(Collectors.groupingBy(Contact::getCountryID, Collectors.counting()));

            List<Country> countryList = countryService.getByIDIn(contactCountByCountry.keySet());
            Map<String, Long> countryStatistics = countryList.stream().
                    collect(Collectors.toMap(Country::getName, e -> contactCountByCountry.get(e.getId())));
            countryStatistics.put("Не указано", contactCountByCountry.get(0));

            List<UserGroups> userGroupsList = userGroupsService.getByLogin(loginUser);
            Map<Integer, Long> contactCountByGroup = contactGroupsService.getByGroupIdIn(
                    userGroupsList.stream().map(UserGroups::getId).collect(Collectors.toList()))
                    .stream().collect(Collectors.groupingBy(ContactGroups::getGroupId, Collectors.counting()));
            Map<String, Long> groupsStatistics = userGroupsList
                    .stream()
                    .collect(Collectors.toMap(UserGroups::getGroupName, e -> {
                                return contactCountByGroup.get(e.getId()) == null ? 0L : contactCountByGroup.get(e.getId());
                            }
                    ));

            String []ageStatisticsLabels = {"до 18", "от 18 до 21", "от 21 до 24", "от 24 до 27", "от 27 до 30", "от 30 до 35", "от 35 до 40", "от 45"};
            contactList = contactList.stream().filter(e -> e.getBirthYear() != 0).collect(Collectors.toList());

            List<Contact> agedMen = contactList.stream().filter(e -> e.getGender() == ContactUtils.GENDER_MAN).collect(Collectors.toList());
            List<Contact> agedWomen = contactList.stream().filter(e -> e.getGender() == ContactUtils.GENDER_WOMAN).collect(Collectors.toList());

            int []menCount = calcAgeStatistics(agedMen, ageStatisticsLabels.length);
            int []womenCount = calcAgeStatistics(agedWomen, ageStatisticsLabels.length);

            request.setAttribute("countryStatistics", mapper.writeValueAsString(countryStatistics));
            request.setAttribute("groupsStatistics", mapper.writeValueAsString(groupsStatistics));
            request.setAttribute("ageStatisticsLabels", mapper.writeValueAsString(ageStatisticsLabels));
            request.setAttribute("menAgeStatistics", mapper.writeValueAsString(menCount));
            request.setAttribute("womenAgeStatistics", mapper.writeValueAsString(womenCount));
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        } catch (JsonProcessingException e) {
            LOG.error("can't convert data to JSON", e);
            throw new CommandExecutionException("error while converting data to JSON", e);
        }
        return "user";
    }
}
