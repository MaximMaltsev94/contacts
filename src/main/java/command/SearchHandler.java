package command;

import dao.interfaces.*;
import dao.mysqlimplementation.*;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by maxim on 30.09.2016.
 */
public class SearchHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(SearchHandler.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = MySqlConnectionFactory.getInstance().getConnection();){
            RelationshipDao rshDao = new MySqlRelationshipDao(connection);
            List<Relationship> relationshipList = rshDao.getAll();
            request.setAttribute("relationshipList", relationshipList);

            CountryDao countryDao = new MySqlCountryDao(connection);
            List<Country> countryList = countryDao.getAll();
            request.setAttribute("countryList", countryList);

            CityDao cityDao = new MySqlCityDao(connection);
            List<City> cityList = cityDao.getAll();
            request.setAttribute("cityList", cityList);

        } catch (NumberFormatException ex) {
            LOG.warn("incorrect contact id {}", request.getParameter("id"), ex);
        }catch (NamingException | SQLException e) {
            LOG.warn("can't get db connection", e);
        } finally {
            request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
        }
    }
}
