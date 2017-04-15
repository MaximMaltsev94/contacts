package dao.implementation;

import dao.interfaces.UserGroupsDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.UserGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DaoUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
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
    public UserGroups getByIdAndLoginUser(int id, String loginUser) throws DaoException {
        LOG.info("selecting user group by id - {}", id);
        String sql = String.format("SELECT * FROM %s WHERE `id` = ? and `login` = ?", TABLE_NAME);
        return jdbcTemplate.queryForObject(rsMapper, sql, id, loginUser);
    }

    @Override
    public List<UserGroups> getByIdIn(List<Integer> idList) throws DaoException {
        LOG.info("selecting user groups by id list - {}", idList);
        if(idList.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = String.format("SELECT * FROM %s WHERE `id` %s", TABLE_NAME, DaoUtils.generateSqlInPart(idList.size()));
        return jdbcTemplate.queryForList(rsMapper, sql, idList.toArray());
    }

    @Override
    public UserGroups insert(UserGroups userGroups) throws DaoException {
        LOG.info("inserting user group - {}", userGroups);
        String sql = String.format("INSERT INTO %s (`group_name`, `login`) VALUES (?, ?)", TABLE_NAME);
        List<Integer> generatedKeys = new ArrayList<>();
        jdbcTemplate.update(sql, generatedKeys, userGroups.getGroupName(), userGroups.getLogin());
        return new UserGroups(generatedKeys.get(0), userGroups.getGroupName(), userGroups.getLogin());
    }

    @Override
    public void update(UserGroups userGroups) throws DaoException {
        LOG.info("updating user group - {}", userGroups);
        String sql = String.format("UPDATE %s SET `group_name` = ?, `login` = ? where `id` = ?", TABLE_NAME);
        jdbcTemplate.update(sql, userGroups.getGroupName(), userGroups.getLogin(), userGroups.getId());
    }

    @Override
    public void delete(List<Integer> idList) throws DaoException {
        LOG.info("deleting user group by id list - {}", idList);
        if(idList.isEmpty())
            return;
        String sql = String.format("DELETE FROM %s WHERE `id` %s", TABLE_NAME, DaoUtils.generateSqlInPart(idList.size()));
        jdbcTemplate.update(sql, idList.toArray());
    }
}
