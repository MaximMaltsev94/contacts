package service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.queries.users.UserField;
import model.Contact;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import util.ContactUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VKServiceImpl implements VKService {
    @Override
    public List<Contact> getFriendsPart(UserActor userActor, int pageNumber, int count, String loginUser) throws ClientException, ApiException {

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        return vk.friends()
                .get(userActor, UserField.NICKNAME, UserField.SEX, UserField.BDATE, UserField.RELATION, UserField.SITE, UserField.CAREER, UserField.PHOTO_200, UserField.COUNTRY, UserField.CITY)
                .count(count)
                .offset((pageNumber - 1) * count)
                .execute()
                .getItems()
                .stream()
                .filter(e -> Objects.isNull(e.getDeactivated()))
                .map(friend -> {
                    Contact contact = new Contact();
                    contact.setId(friend.getId());
                    contact.setFirstName(friend.getFirstName());
                    contact.setLastName(friend.getLastName());
                    contact.setPatronymic(friend.getNickname());
                    switch (friend.getSex()) {
                        case UNKNOWN:
                            contact.setGender(ContactUtils.GENDER_ANY);
                            break;
                        case MALE:
                            contact.setGender(ContactUtils.GENDER_MAN);
                            break;
                        case FEMALE:
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
                    if(friend.getPhoto200() == null) {
                        if(contact.getGender() == ContactUtils.GENDER_WOMAN) {
                            contact.setProfilePicture("/sysImages/girl.png");
                        } else {
                            contact.setProfilePicture("/sysImages/default.png");
                        }
                    } else {
                        contact.setProfilePicture(friend.getPhoto200());
                    }
                    if(friend.getCountry() != null) {
                        contact.setCountryID(friend.getCountry().getId());
                    }
                    if(friend.getCity() != null) {
                        contact.setCityID(friend.getCity().getId());
                    }
//                        contact.setStreet(friend.get);
//                        contact.setPostcode();
                    contact.setLoginUser(loginUser);
                    return contact;
                })
                .collect(Collectors.toList());
    }
}
