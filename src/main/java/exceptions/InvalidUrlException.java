package exceptions;

import java.net.MalformedURLException;

public class InvalidUrlException extends Exception {
    public InvalidUrlException(String message, MalformedURLException cause) {
        super(message, cause);
    }
}
