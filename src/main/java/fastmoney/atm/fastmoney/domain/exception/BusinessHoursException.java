package fastmoney.atm.fastmoney.domain.exception;

public class BusinessHoursException extends RuntimeException {

    public BusinessHoursException() {
        super("Amount not allowed between 8PM and 5AM! The maximum amount is R$500,00");
    }
}
