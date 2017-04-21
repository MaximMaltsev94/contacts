package dao.implementation;

import dao.interfaces.CityDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DaoUtils;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class CityDaoImpl implements CityDao {
    private static final Logger LOG = LoggerFactory.getLogger(CityDaoImpl.class);
    private final String TABLE_NAME = "`city`";
    private ResultSetMapper<City> rsMapper;
    private JdbcTemplate<City> jdbcTemplate;

    public CityDaoImpl(Connection connection) {
        this.jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            City city = new City();
            city.setId(rs.getInt("id"));
            city.setName(rs.getString("name"));
            city.setCountryID(rs.getInt("id_country"));
            return city;
        };
    }

    @Override
    public List<City> getByIDIn(List<Integer> idList) throws DaoException {
        LOG.info("selecting cities by id list - {}", idList);
        if(idList.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = String.format("SELECT * FROM %s WHERE `id` %s", TABLE_NAME, DaoUtils.generateSqlInPart(idList.size()));
        return jdbcTemplate.queryForList(rsMapper, sql, idList.toArray());
    }
}
