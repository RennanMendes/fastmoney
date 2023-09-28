package fastmoney.atm.fastmoney.domain.validation;

import fastmoney.atm.fastmoney.domain.dto.transaction.ValidationDto;
import fastmoney.atm.fastmoney.domain.exception.InvalidValueException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionValueValidation implements TransactionValidation {

    @Override
    public void validate(ValidationDto validationDto) {
        if (validationDto.value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException();
        }
    }

}
