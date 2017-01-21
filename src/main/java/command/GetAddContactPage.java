package command;

import dao.implementation.CityDaoImpl;
import dao.implementation.ConnectionFactory;
import dao.implementation.CountryDaoImpl;
import dao.implementation.RelationshipDaoImpl;
import dao.interfaces.CityDao;
import dao.interfaces.CountryDao;
import dao.interfaces.RelationshipDao;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.City;
import model.Country;
import model.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetAddContactPage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetAddContactPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_PAGE = "addContact";

        try {
            RelationshipDao rshDao = new RelationshipDaoImpl(connection);
            List<Relationship> relationshipList = rshDao.getAll();
            request.setAttribute("relationshipList", relationshipList);

            CountryDao countryDao = new CountryDaoImpl(connection);
            List<Country> countryList = countryDao.getAll();
            request.setAttribute("countryList", countryList);

            CityDao cityDao = new CityDaoImpl(connection);
            List<City> cityList = cityDao.getAll();
            request.setAttribute("cityList", cityList);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        }

        return VIEW_PAGE;
    }
}
