package fastmoney.atm.fastmoney.domain.exception;

public class InvalidBalanceException extends RuntimeException {

    public InvalidBalanceException() {
        super("Invalid balance");
    }
}
