package command;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.queries.users.UserField;
import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactUtils;
import util.RequestUtils;
import util.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class GetImportVkPage implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(GetImportVkPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_NAME = "editList";
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("../importVK.properties"));

            String code = (String) request.getAttribute("code");
            int appId = Integer.parseInt(properties.getProperty("client_id"));
            String clientSecret = properties.getProperty("app_secret");
            String uri = String.format("http://%s:%s%s/%s", request.getServerName(), request.getServerPort(), request.getContextPath(), properties.getProperty("redirect_uri"));
            UserAuthResponse authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(appId, clientSecret, uri, code)
                    .execute();

            UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());


            List<Contact> contactList = vk.friends()
                    .get(actor, UserField.NICKNAME, UserField.SEX, UserField.BDATE, UserField.RELATION, UserField.SITE, UserField.CAREER, UserField.PHOTO_200, UserField.COUNTRY, UserField.CITY)
                    .count(10)
                    .execute()
                    .getItems()
                    .stream()
                    .map(friend -> {
                        Contact contact = new Contact();
                        contact.setId(friend.getId());
                        contact.setFirstName(friend.getFirstName());
                        contact.setLastName(friend.getLastName());
                        contact.setPatronymic(friend.getNickname());
                        switch (friend.getSex().getValue()) {
                            case ContactUtils.GENDER_ANY:
                                contact.setGender(ContactUtils.GENDER_ANY);
                                break;
                            case ContactUtils.GENDER_MAN:
                                contact.setGender(ContactUtils.GENDER_MAN);
                                break;
                            case ContactUtils.GENDER_WOMAN:
                                contact.setGender(ContactUtils.GENDER_WOMAN);
                                break;
                        }

                        String db[] = StringUtils.split(friend.getBdate(), ".");
                        if(db != null) {
                            if (db.length > 0)
                                contact.setBirthDay(Integer.parseInt(db[0]));
                            if (db.length > 1)
                                contact.setBirthMonth(Integer.parseInt(db[1]));
                            if (db.length > 2)
                                contact.setBirthYear(Integer.parseInt(db[2]));
                        }

                        contact.setRelationshipID(friend.getRelation() == null ? 0 : friend.getRelation());
//                        contact.setCitizenship();
                        contact.setWebSite(friend.getSite());
//                        contact.setEmail();
                        if(CollectionUtils.isNotEmpty(friend.getCareer())) {
                            contact.setCompanyName(friend.getCareer().get(0).getCompany());
                        }
                        contact.setProfilePicture(friend.getPhoto200());
                        if(friend.getCountry() != null) {
                            contact.setCountryID(friend.getCountry().getId());
                        }
                        if(friend.getCity() != null) {
                            contact.setCityID(friend.getCity().getId());
                        }
//                        contact.setStreet(friend.get);
//                        contact.setPostcode();
                        contact.setLoginUser(request.getUserPrincipal().getName());
                        return contact;
                    })
                    .collect(Collectors.toList());

            request.setAttribute("action", "importVK");
            request.getSession().setAttribute("friendList", contactList);
            request.setAttribute("contactList", contactList.stream().map(e -> {
                Contact contact = new Contact();
                contact.setId(e.getId());
                contact.setFirstName(e.getFirstName());
                contact.setLastName(e.getLastName());
                contact.setProfilePicture(e.getProfilePicture());
                return contact;
            }).collect(Collectors.toList()));

// TODO: 22.04.17 добавить мобильные телефоны
        } catch (IOException e) {
            LOG.error("can't load properties", e);
            return null;
        } catch (ApiException | ClientException e) {
            LOG.error("can't access vk", e);
            RequestUtils.setMessageText(request, "Произошла ошибка при получении данных Вконтакте", TooltipType.danger);
            return null;
        }

        return VIEW_NAME;
    }
}
