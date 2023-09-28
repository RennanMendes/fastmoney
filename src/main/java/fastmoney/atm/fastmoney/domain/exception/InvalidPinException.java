package fastmoney.atm.fastmoney.domain.exception;

public class InvalidPinException extends RuntimeException {

    public InvalidPinException() {
        super("Incorrect pin");
    }
}
