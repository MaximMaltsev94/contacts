package dao.implementation;

import dao.interfaces.UserDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);
    private final String TABLE_NAME = "\"user\"";
    private ResultSetMapper<User> rsMapper;
    private JdbcTemplate<User> jdbcTemplate;

    public UserDaoImpl(Connection connection) {
        this.jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            User user = new User();
            user.setLogin(rs.getString("login"));
            user.setEmail(rs.getString("email"));
            user.setProfilePicture(rs.getString("profile_picture"));
            user.setNeedBDateNotify(rs.getBoolean("need_bdate_notify"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        };
    }

    @Override
    public User getByLogin(String login) throws DaoException {
        LOG.info("selecting user by login - {}", login);
        String sql = String.format("SELECT * FROM %s WHERE login = ?", TABLE_NAME);
        return jdbcTemplate.queryForObject(rsMapper, sql, login);
    }

    @Override
    public User getByEmail(String email) throws DaoException {
        LOG.info("selecting user by email - {}", email);
        String sql = String.format("SELECT * FROM %s where \"email\" = ?", TABLE_NAME);
        return jdbcTemplate.queryForObject(rsMapper, sql, email);
    }

    @Override
    public List<User> getByNeedNotify(boolean needBDateNotify) throws DaoException {
        LOG.info("selecting users by birth date notify - {}", needBDateNotify);
        String sql = String.format("SELECT * FROM %s WHERE \"need_bdate_notify\" = ?", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql, needBDateNotify);
    }

    @Override
    public void insert(User user) throws DaoException {
        LOG.info("inserting user - {}", user);
        String sql = String.format("INSERT INTO %s (\"login\", \"email\", \"need_bdate_notify\", \"profile_picture\", \"password\") VALUES(?, ?, ?, ?, ?)", TABLE_NAME);
        jdbcTemplate.update(sql, user.getLogin(),
                                    user.getEmail(),
                                    user.getNeedBDateNotify(),
                                    user.getProfilePicture(),
                                    user.getPassword());
    }

    @Override
    public void update(User user) throws DaoException {
        LOG.info("updating user - {}", user);
        String sql = String.format("UPDATE %s SET \"login\" = ?, \"email\" = ?, \"need_bdate_notify\" = ?, \"profile_picture\" = ?, \"password\" = ? WHERE \"login\" = ?", TABLE_NAME);
        jdbcTemplate.update(sql, user.getLogin(),
                user.getEmail(),
                user.getNeedBDateNotify(),
                user.getProfilePicture(),
                user.getPassword(),
                user.getLogin());
    }

    @Override
    public void delete(String login) throws DaoException {
        LOG.info("deleting user by login - {}", login);
        String sql = String.format("DELETE FROM %s WHERE \"login\" = ?", TABLE_NAME);
        jdbcTemplate.update(sql, login);
    }
}
