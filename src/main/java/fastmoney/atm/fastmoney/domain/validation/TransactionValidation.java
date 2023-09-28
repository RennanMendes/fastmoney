package fastmoney.atm.fastmoney.domain.validation;

import fastmoney.atm.fastmoney.domain.dto.transaction.ValidationDto;

public interface TransactionValidation {
    void validate(ValidationDto validationDto);

}
