package service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VKServiceImpl implements VKService {
    private VkApiClient vk;
    private UserActor userActor;

    public VKServiceImpl(UserActor userActor) {
        vk = new VkApiClient(HttpTransportClient.getInstance());
        this.userActor = userActor;
    }

    @Override
    public List<Integer> getFriends() throws ClientException, ApiException {
        return vk.friends().get(userActor).execute().getItems();
    }

    @Override
    public List<UserXtrCounters> getFriendsByIdIn(List<Integer> userIdList) throws ClientException, ApiException {
        return vk.users()
                .get(userActor)
                .fields(UserField.NICKNAME, UserField.SEX, UserField.BDATE, UserField.RELATION, UserField.SITE, UserField.CAREER, UserField.PHOTO_200, UserField.COUNTRY, UserField.CITY)
                .userIds(userIdList.stream().map(Objects::toString).collect(Collectors.toList()))
                .execute();
    }
}
