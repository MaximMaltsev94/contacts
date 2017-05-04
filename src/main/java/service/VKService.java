package service;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;

import java.util.List;

public interface VKService {
    List<Integer> getFriends() throws ClientException, ApiException;
    List<UserXtrCounters> getFriendsByIdIn(List<Integer> userIdList) throws ClientException, ApiException;
}
