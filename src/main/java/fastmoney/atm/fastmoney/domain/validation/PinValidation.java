package fastmoney.atm.fastmoney.domain.validation;

import fastmoney.atm.fastmoney.domain.dto.transaction.ValidationDto;
import fastmoney.atm.fastmoney.domain.exception.InvalidPinException;
import org.springframework.stereotype.Component;

@Component
public class PinValidation implements TransactionValidation {

    @Override
    public void validate(ValidationDto validationDto) {
        if (validationDto.user().getAccount().getPin().compareTo(validationDto.pin()) != 0) {
            throw new InvalidPinException();
        }
    }
}
