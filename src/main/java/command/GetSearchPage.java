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

public class GetSearchPage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetSearchPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_NAME = "search";

        RelationshipService relationshipService = new RelationshipServiceImpl(connection);
        CountryService countryService = new CountryServiceImpl(connection);

        try {
            request.setAttribute("relationshipList", relationshipService.getAll());
            request.setAttribute("countryList", countryService.getAll());

            request.setAttribute("gender", 2);
            request.setAttribute("age1", 0);
            request.setAttribute("age2", 0);
            request.setAttribute("country", 0);
            request.setAttribute("city", 0);
            request.setAttribute("relationship", 0);

        }  catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        }

        return VIEW_NAME;
    }
}
