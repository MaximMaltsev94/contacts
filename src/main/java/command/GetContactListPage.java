package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import model.ContactGroups;
import model.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class GetContactListPage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetContactListPage.class);

    private final int CONTACTS_PER_PAGE = 10;

    private int parseFilterAttribute(HttpServletRequest request) {
        String filter = null;
        try {
            filter = (String) request.getAttribute("filter");
            if(StringUtils.isNoneBlank(filter)) {
                return Integer.parseInt(filter);
            }
        } catch (NumberFormatException e) {
            LOG.error("incorrect filter - {}", filter);
        }
        return -1;
    }


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_PAGE = "contacts";

        ContactService contactService = new ContactServiceImpl(connection);
        UserGroupsService userGroupsService = new UserGroupsServiceImpl(connection);
        ContactGroupsService contactGroupsService = new ContactGroupsServiceImpl(connection);

        try {
            int pageNumber = Integer.parseInt((String) request.getAttribute("page"));
            String userLogin = request.getUserPrincipal().getName();
            int groupId = parseFilterAttribute(request);

            Page<Contact> contactPage = null;
            if(groupId > 0) {
                List<ContactGroups> contactGroupsList = contactGroupsService.getByGroupId(groupId);
                List<Integer> contactIdList = contactGroupsList.stream().map(ContactGroups::getContactID).collect(Collectors.toList());
                contactPage = contactService.getByIdInAndLoginUser(pageNumber, CONTACTS_PER_PAGE, contactIdList, userLogin);
            } else {
                contactPage = contactService.getByLoginUser(pageNumber, CONTACTS_PER_PAGE, userLogin);
                request.removeAttribute("filter");
            }

            long maxPageNumber = (long)Math.ceil((double) contactPage.getTotalRowCount() / (double) CONTACTS_PER_PAGE);

            maxPageNumber = Math.max(maxPageNumber, 1);

            if (pageNumber > maxPageNumber || pageNumber < 1) {
                throw new NumberFormatException();
            }

            List<Integer> selectedContactIdList = contactPage.getData().stream().map(Contact::getId).collect(Collectors.toList());

            request.setAttribute("contactList", contactPage.getData());
            request.setAttribute("maxPageNumber", maxPageNumber);
            request.setAttribute("userGroups", userGroupsService.getByLogin(userLogin));
            request.setAttribute("contactGroups", contactService.getContactGroups(selectedContactIdList));

            request.getSession().setAttribute("lastVisitedPage", pageNumber);
            if(groupId != -1) {
                request.getSession().setAttribute("lastVisitedFilter", groupId);
            } else {
                request.getSession().removeAttribute("lastVisitedFilter");
            }

        } catch (NumberFormatException e) {
            LOG.error("incorrect page number - {}", request.getAttribute("page"), e);
            throw new DataNotFoundException("can't find contacts by page number - " + request.getAttribute("page"), e);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }

        return VIEW_PAGE;
    }
}
