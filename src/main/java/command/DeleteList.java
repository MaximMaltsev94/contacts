package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserGroupsService;
import service.UserGroupsServiceImpl;
import util.request.RequestUtils;
import util.request.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteList implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteList.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        UserGroupsService userGroupsService = new UserGroupsServiceImpl(connection);
        try {

            List<Integer> idList = Arrays.stream(request.getParameter("id").split(","))
                    .map(id -> StringUtils.substringAfter(id, "manage-group-"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            userGroupsService.delete(idList);
            RequestUtils.setMessageText(request, "Выбраные списки успешно удалены", TooltipType.success);
        } catch (DaoException e) {
            LOG.info("can't delete user group with id - {}", request.getAttribute("id"), e);
            throw new CommandExecutionException("error while accessing database", e);
        }
        return null;
    }
}
