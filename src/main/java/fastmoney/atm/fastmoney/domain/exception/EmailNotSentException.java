package fastmoney.atm.fastmoney.domain.exception;

import java.io.IOException;

public class EmailNotSentException extends RuntimeException {
    public EmailNotSentException(IOException ex) {
        super("Email not sent!", ex);
    }
}
