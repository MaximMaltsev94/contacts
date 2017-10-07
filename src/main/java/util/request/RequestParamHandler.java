package util.request;

import javax.servlet.http.HttpServletRequest;

import exceptions.RequestParamHandlerException;

public interface RequestParamHandler {

    void handleRequestParams(HttpServletRequest request) throws RequestParamHandlerException;

}
