package dao.implementation;

import dao.interfaces.UserRolesDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class UserRolesDaoImpl implements UserRolesDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserRolesDaoImpl.class);
    private final String TABLE_NAME = "`user_roles`";
    private ResultSetMapper<UserRoles> rsMapper;
    private JdbcTemplate<UserRoles> jdbcTemplate;

    public UserRolesDaoImpl(Connection connection) {
        this.jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            UserRoles userRoles = new UserRoles();
            userRoles.setLogin(rs.getString("login"));
            userRoles.setRoleName(rs.getString("role_name"));
            return userRoles;
        };
    }

    @Override
    public List<UserRoles> getByLogin(String login) throws DaoException {
        LOG.info("selecting user roles by login - {}", login);
        String sql = String.format("SELECT * FROM %s WHERE login = ?", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql, login);
    }

    @Override
    public void insert(UserRoles userRoles) throws DaoException {
        LOG.info("inserting user role - {}", userRoles);
        String sql = String.format("INSERT INTO %s VALUES(?, ?)", TABLE_NAME);
        jdbcTemplate.update(sql, userRoles.getLogin(), userRoles.getRoleName());
    }
}
