package app.tozzi.model.exception;

public class MailParserException extends RuntimeException {

    public MailParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailParserException(Throwable cause) {
        super(cause);
    }

    public MailParserException(String message) {
        super(message);
    }
}