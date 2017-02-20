package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.UserServiceImpl;
import util.RequestUtils;
import util.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class UpdateUser implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateUser.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        UserService userService = new UserServiceImpl(connection);

        String imageAction = "nothing";
        boolean isErrorOccurred = true;
        User newUser = null;
        User oldUser = null;

        try {
            newUser = userService.parseRequest(request);
            oldUser = userService.getByLogin(request.getUserPrincipal().getName());

            imageAction = (String) request.getAttribute("imageAction");
            switch (imageAction) {
                case "update": //newContact.profileImage contains new image url
                case "delete": //newContact.profileImage contains url to default image
                    break;
                case "nothing": //
                    newUser.setProfilePicture(oldUser.getProfilePicture());
                    break;
            }

            if(newUser.getPassword() == null) {
                newUser.setPassword(oldUser.getPassword());
            }

            userService.update(newUser);
            isErrorOccurred = false;
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        } finally {
            if(isErrorOccurred) {
                RequestUtils.setMessageText(request, "Произошла ошибка при редактировании пользователя " + request.getUserPrincipal().getName() + ". Информация не изменена", TooltipType.danger);
                if(newUser != null) {
                    userService.deleteProfileImageFile(newUser);
                }

            } else {
                RequestUtils.setMessageText(request, "Информация пользователя " + request.getUserPrincipal().getName() + " успешно изменена", TooltipType.success);
                if(!imageAction.equals("nothing"))
                    userService.deleteProfileImageFile(oldUser);
            }
        }


        return null;
    }
}
