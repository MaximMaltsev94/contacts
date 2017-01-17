package exceptions;

public class RequestMapperException extends Exception {
    public RequestMapperException() {
        super();
    }

    public RequestMapperException(String message) {
        super(message);
    }

    public RequestMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestMapperException(Throwable cause) {
        super(cause);
    }
}
