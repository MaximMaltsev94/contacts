package service;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.UserXtrLists;

import java.util.List;

public interface VKService {
    List<UserXtrLists> getFriendsPart(UserActor userActor, int pageNumber, int count) throws ClientException, ApiException;
}
