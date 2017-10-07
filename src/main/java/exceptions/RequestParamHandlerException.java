package exceptions;

public class RequestParamHandlerException extends Exception {
    public RequestParamHandlerException() {
        super();
    }

    public RequestParamHandlerException(String message) {
        super(message);
    }

    public RequestParamHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestParamHandlerException(Throwable cause) {
        super(cause);
    }
}
