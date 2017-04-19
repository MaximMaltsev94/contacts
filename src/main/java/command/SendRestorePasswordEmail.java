package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserResetTokensService;
import service.UserResetTokensServiceImpl;
import service.UserService;
import service.UserServiceImpl;
import util.EmailHelper;
import util.RequestUtils;
import util.TooltipType;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class SendRestorePasswordEmail implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(SendRestorePasswordEmail.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        UserService userService = new UserServiceImpl(connection);
        UserResetTokensService userResetTokensService = new UserResetTokensServiceImpl(connection);
        EmailHelper emailHelper = new EmailHelper();

        try {
            String login = (String)request.getAttribute("login");
            User user = userService.getByLogin(login);
            if(user == null) {
                RequestUtils.setMessageText(request, String.format("Пользователь с логином %s не зарегистрирован", login), TooltipType.danger);
                return null;
            }

            String token = userResetTokensService.createToken(user);

            Map<String, Object> args = new HashMap<>();
            args.put("login", user.getLogin());
            args.put("token", token);

            Map.Entry<String, String> template = emailHelper.readTemplateFile("restorePasswordEmail.txt");
            if(template == null) {
                RequestUtils.setMessageText(request, "Произошла ошибка при отправке email с инструкциями", TooltipType.danger);
                return null;
            }
            String message = emailHelper.processTemplate(template.getValue(), args);

            emailHelper.sendEmail(user.getEmail(), template.getKey(), message);
            RequestUtils.setMessageText(request, "Инструкции по восстановлению пароля отправлеы на почту, указанную при регистрации", TooltipType.success);
        } catch (MessagingException e) {
            LOG.error("error while sending restore password email", e);
            RequestUtils.setMessageText(request, "Не удалось отправить email с инструкциями по восстановлению пароля", TooltipType.danger);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }

        return null;
    }
}
