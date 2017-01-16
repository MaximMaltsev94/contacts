package command;

import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
    String execute(HttpServletRequest request, HttpServletResponse response) throws CommandExecutionException, DataNotFoundException;
}
