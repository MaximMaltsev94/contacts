package service;

import exceptions.DaoException;
import model.User;
import model.UserResetTokens;

public interface UserResetTokensService {
    String createToken(User user) throws DaoException;

    UserResetTokens get(String token) throws DaoException;

    boolean isValid(UserResetTokens userResetTokens);

    void removeInvalidTokens() throws DaoException;
}
