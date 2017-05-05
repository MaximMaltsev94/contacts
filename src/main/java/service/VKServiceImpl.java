package service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.ServiceClientCredentialsFlowResponse;
import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import util.VkProperties;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VKServiceImpl implements VKService {
    private VkApiClient vk;
    private UserActor userActor;
    private ServiceActor serviceActor;

    public VKServiceImpl(UserActor userActor) throws IOException {
        vk = new VkApiClient(HttpTransportClient.getInstance());
        this.userActor = userActor;
        VkProperties vkProperties = new VkProperties();
        this.serviceActor = new ServiceActor(vkProperties.getApllicationId(), vkProperties.getAppSecret(), vkProperties.getServiceSecret());
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

    @Override
    public List<BaseObject> getCitiesById(List<Integer> cityIdList) throws ClientException, ApiException {
        return vk.database().getCitiesById(serviceActor).cityIds(cityIdList).execute();
    }
}
