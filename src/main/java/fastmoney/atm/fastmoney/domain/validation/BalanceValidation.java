package fastmoney.atm.fastmoney.domain.validation;

import fastmoney.atm.fastmoney.domain.dto.transaction.ValidationDto;
import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;
import fastmoney.atm.fastmoney.domain.exception.InvalidBalanceException;
import org.springframework.stereotype.Component;

@Component
public class BalanceValidation implements TransactionValidation {

    @Override
    public void validate(ValidationDto validationDto) {
        if (validationDto.type() == TransactionType.OUTPUT) {
            if (validationDto.value().compareTo(validationDto.user().getAccount().getBalance()) > 0) {
                throw new InvalidBalanceException();
            }
        }
    }

}
