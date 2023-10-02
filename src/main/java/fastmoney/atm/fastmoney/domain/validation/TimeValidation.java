package fastmoney.atm.fastmoney.domain.validation;

import fastmoney.atm.fastmoney.domain.dto.transaction.ValidationDto;
import fastmoney.atm.fastmoney.domain.exception.BusinessHoursException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;

@Component
public class TimeValidation implements TransactionValidation {

    @Override
    public void validate(ValidationDto validationDto) {
        LocalTime now = LocalTime.now();
        LocalTime startTime = LocalTime.of(20, 0);
        LocalTime endTime = LocalTime.of(5, 0);

        if (now.isAfter(startTime) || now.isBefore(endTime)) {
            if (validationDto.value().compareTo(new BigDecimal(500)) > 0) {
                throw new BusinessHoursException();
            }
        }
    }

}
