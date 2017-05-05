package service;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.base.BaseObject;
import exceptions.DaoException;
import model.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CityServiceVkImpl implements CityService {
    private static final Logger LOG = LoggerFactory.getLogger(CityServiceVkImpl.class);

    private VKService vkService;

    public CityServiceVkImpl(VKService vkService) {
        this.vkService = vkService;
    }

    @Override
    public List<City> getByIDIn(List<Integer> idList) throws DaoException {
        try {
            List<BaseObject> cities = vkService.getCitiesById(idList);
            return cities.stream().map(city -> new City(city.getId(), city.getTitle())).collect(Collectors.toList());
        } catch (ClientException | ApiException e) {
            LOG.error("can't access vk database", e);
            throw new DaoException("error while accessing VK database", e);
        }
    }
}
