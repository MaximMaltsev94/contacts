package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserResetTokens extends GenericModel implements Serializable {
    private String token;
    private String login;
    private LocalDateTime expiryDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public UserResetTokens() {
    }

    public UserResetTokens(String token, String login, LocalDateTime expiryDate) {
        this.token = token;
        this.login = login;
        this.expiryDate = expiryDate;
    }
}
