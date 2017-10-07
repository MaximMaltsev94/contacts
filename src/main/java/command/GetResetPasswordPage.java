package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.UserResetTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserResetTokensService;
import service.UserResetTokensServiceImpl;
import util.request.RequestUtils;
import util.request.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class GetResetPasswordPage implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(GetResetPasswordPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_NAME = "resetPassword";
        UserResetTokensService userResetTokensService = new UserResetTokensServiceImpl(connection);
        try {
            String token = (String) request.getAttribute("token");
            if (token  == null) {
                RequestUtils.setMessageText(request, "Невалидный токен для восстановления пароля", TooltipType.danger);
                return null;
            }

            UserResetTokens userResetTokens = userResetTokensService.get(token);
            if(userResetTokens == null) {
                RequestUtils.setMessageText(request, "Невалидный токен для восстановления пароля", TooltipType.danger);
                return null;
            }

            if(!userResetTokensService.isValid(userResetTokens)) {
                RequestUtils.setMessageText(request, "Срок действия токена истек", TooltipType.danger);
                return null;
            }
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }
        return VIEW_NAME;
    }
}
