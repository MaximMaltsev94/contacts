package dao.implementation;

import dao.interfaces.UserGroupsDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.UserGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class UserGroupsDaoImpl implements UserGroupsDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserGroupsDaoImpl.class);
    private final String TABLE_NAME = "`user_groups`";
    private ResultSetMapper<UserGroups> rsMapper;
    private JdbcTemplate<UserGroups> jdbcTemplate;

    public UserGroupsDaoImpl(Connection connection) {
        this.jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            UserGroups userGroups = new UserGroups();
            userGroups.setId(rs.getInt("id"));
            userGroups.setGroupName(rs.getString("group_name"));
            userGroups.setLogin(rs.getString("login"));
            return userGroups;
        };
    }

    @Override
    public List<UserGroups> getByLogin(String login) throws DaoException {
        LOG.info("selecting user groups by login - {}", login);
        String sql = String.format("SELECT * FROM %s WHERE `login` = ?", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql, login);
    }

    @Override
    public void insert(UserGroups userGroups) throws DaoException {
        LOG.info("inserting user group - {}", userGroups);
        String sql = String.format("INSERT INTO %s (`group_name`, `login`) VALUES (?, ?)", TABLE_NAME);
        jdbcTemplate.update(sql, userGroups.getGroupName(), userGroups.getLogin());
    }
}
