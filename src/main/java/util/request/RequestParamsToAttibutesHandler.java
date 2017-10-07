package util.request;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import exceptions.RequestParamHandlerException;

public class RequestParamsToAttibutesHandler implements RequestParamHandler {

    @Override
    public void handleRequestParams(HttpServletRequest request) throws RequestParamHandlerException {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            request.setAttribute(name, request.getParameter(name));
        }
    }
}
