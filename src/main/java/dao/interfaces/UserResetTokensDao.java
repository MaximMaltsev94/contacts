package dao.interfaces;

import exceptions.DaoException;
import model.UserResetTokens;

import java.time.LocalDateTime;

public interface UserResetTokensDao {
    void insert(UserResetTokens userResetTokens) throws DaoException;

    UserResetTokens get(String token) throws DaoException;

    void deleteWhereExpiryDateLessThan(LocalDateTime dateTime) throws DaoException;
}
