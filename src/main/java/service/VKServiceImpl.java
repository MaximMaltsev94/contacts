package service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.queries.users.UserField;

import java.util.List;

public class VKServiceImpl implements VKService {
    @Override
    public List<UserXtrLists> getFriendsPart(UserActor userActor, int pageNumber, int count) throws ClientException, ApiException {

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        return vk.friends()
                .get(userActor, UserField.NICKNAME, UserField.SEX, UserField.BDATE, UserField.RELATION, UserField.SITE, UserField.CAREER, UserField.PHOTO_200, UserField.COUNTRY, UserField.CITY)
                .count(count)
                .offset((pageNumber - 1) * count)
                .execute()
                .getItems();
    }
}
