package common;

/**
 * Exception thrown when row or col parameter is incorrect
 */
public class ParamException extends Exception {
    ParamException(String message) {
        super(message);
    }
}
