package fastmoney.atm.fastmoney.domain.validation;

import fastmoney.atm.fastmoney.domain.dto.transaction.ValidationDto;
import fastmoney.atm.fastmoney.domain.exception.BusinessHoursException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;

@Component
public class TimeValidation implements TransactionValidation{
    @Override
    public void validate(ValidationDto validationDto) {
        LocalTime startTime = LocalTime.of(20, 0);
        LocalTime endTime = LocalTime.of(5, 0);

        if(validationDto.now().isAfter(startTime)|| validationDto.now().isBefore(endTime)){
            if (validationDto.value().compareTo(new BigDecimal(500))>0){
                throw new BusinessHoursException();
            }
        }
    }
}
