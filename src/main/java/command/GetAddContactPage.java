package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class GetAddContactPage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetAddContactPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_PAGE = "editContact";

        RelationshipService relationshipService = new RelationshipServiceImpl(connection);
        CountryService countryService = new CountryServiceImpl(connection);
        CityService cityService = new CityServiceImpl(connection);

        try {
            request.setAttribute("relationshipList", relationshipService.getAll());
            request.setAttribute("countryList", countryService.getAll());
            request.setAttribute("cityList", cityService.getAll());
            request.setAttribute("action", "add");
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        }

        return VIEW_PAGE;
    }
}
