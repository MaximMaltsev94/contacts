package dao.implementation;

import dao.interfaces.UserResetTokensDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.UserResetTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserResetTokensDaoImpl implements UserResetTokensDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserResetTokensDaoImpl.class);
    private String TABLE_NAME = "\"user_reset_tokens\"";
    private DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private ResultSetMapper<UserResetTokens> rsMapper;
    private JdbcTemplate<UserResetTokens> jdbcTemplate;

    public UserResetTokensDaoImpl(Connection connection) {
        jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            UserResetTokens userResetTokens = new UserResetTokens();
            userResetTokens.setToken(rs.getString("token"));
            userResetTokens.setLogin(rs.getString("login"));
            userResetTokens.setExpiryDate(rs.getTimestamp("expiry_date").toLocalDateTime());
            return userResetTokens;
        };
    }

    @Override
    public void insert(UserResetTokens userResetTokens) throws DaoException {
        LOG.info("inserting user reset token - {}", userResetTokens);
        String sql = String.format("INSERT INTO %s (\"token\", \"login\", \"expiry_date\") VALUES (?, ?, ?)", TABLE_NAME);
        jdbcTemplate.update(sql, userResetTokens.getToken(),
                userResetTokens.getLogin(),
                userResetTokens.getExpiryDate().format(DATE_FORMATTER));
    }

    @Override
    public UserResetTokens get(String token) throws DaoException {
        LOG.info("selecting user reset token - {}", token);
        String sql = String.format("SELECT * FROM %s WHERE \"token\" = ?", TABLE_NAME);
        return jdbcTemplate.queryForObject(rsMapper, sql, token);
    }

    @Override
    public void deleteWhereExpiryDateLessThan(LocalDateTime dateTime) throws DaoException {
        LOG.info("deleting user reset tokens where expiry date less than - {}", dateTime);
        String sql = String.format("DELETE FROM %s WHERE (expiry_date < ?::timestamptz) = 't'", TABLE_NAME);
        jdbcTemplate.update(sql, dateTime.format(DATE_FORMATTER));
    }
}
