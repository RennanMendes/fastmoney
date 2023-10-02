package fastmoney.atm.fastmoney.domain.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequestDto(
        @NotNull
        BigDecimal value,
        @NotBlank
        String pin
) {
}
