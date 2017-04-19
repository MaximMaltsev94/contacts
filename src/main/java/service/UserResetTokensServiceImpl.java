package service;

import dao.implementation.UserResetTokensDaoImpl;
import dao.interfaces.UserResetTokensDao;
import exceptions.DaoException;
import model.User;
import model.UserResetTokens;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.time.LocalDateTime;

public class UserResetTokensServiceImpl implements UserResetTokensService {

    private UserResetTokensDao userResetTokensDao;

    public UserResetTokensServiceImpl(Connection connection) {
        userResetTokensDao = new UserResetTokensDaoImpl(connection);
    }

    @Override
    public String createToken(User user) throws DaoException {
        String token = RandomStringUtils.randomAlphanumeric(200);
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);
        userResetTokensDao.insert(new UserResetTokens(token, user.getLogin(), expiryDate));
        return token;
    }

    @Override
    public UserResetTokens get(String token) throws DaoException {
        return userResetTokensDao.get(token);
    }

    @Override
    public boolean isValid(UserResetTokens userResetTokens) {
        return LocalDateTime.now().isBefore(userResetTokens.getExpiryDate());
    }

    @Override
    public void removeInvalidTokens() throws DaoException {
        userResetTokensDao.deleteWhereExpiryDateLessThan(LocalDateTime.now());
    }
}
