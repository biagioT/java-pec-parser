package app.tozzi.model.exception;

/**
 * @author Biagio Tozzi
 */
public class MailParserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

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
