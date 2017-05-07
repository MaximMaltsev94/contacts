package dao.implementation;

import dao.interfaces.CountryDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DaoUtils;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class CountryDaoImpl implements CountryDao {
    private static final Logger LOG = LoggerFactory.getLogger(CountryDaoImpl.class);
    private final String TABLE_NAME = "\"country\"";
    private ResultSetMapper<Country> rsMapper;
    private JdbcTemplate<Country> jdbcTemplate;

    public CountryDaoImpl(Connection connection) {
        this.jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            Country c = new Country();
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
            c.setPhoneCode(rs.getInt("phone_code"));
            return c;
        };
    }

    @Override
    public List<Country> getAll() throws DaoException {
        LOG.info("selecting all countries");
        String sql = String.format("SELECT * FROM %s", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql);
    }

    @Override
    public Country getByID(int countryID) throws DaoException {
        LOG.info("selecting country by id - {}", countryID);
        String sql = String.format("SELECT * FROM %s WHERE \"id\" = ?", TABLE_NAME);
        return jdbcTemplate.queryForObject(rsMapper, sql, countryID);
    }

    @Override
    public List<Country> getByIDIn(List<Integer> idList) throws DaoException {
        LOG.info("selecting countries by id list - {}", idList);
        if(idList.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = String.format("SELECT * FROM %s WHERE \"id\" %s", TABLE_NAME, DaoUtils.generateSqlInPart(idList.size()));
        return jdbcTemplate.queryForList(rsMapper, sql, idList.toArray());
    }
}
