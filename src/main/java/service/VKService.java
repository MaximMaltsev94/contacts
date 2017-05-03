package service;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import model.Contact;

import java.util.List;

public interface VKService {
    List<Contact> getFriendsPart(UserActor userActor, int pageNumber, int count, String loginUser) throws ClientException, ApiException;
}
