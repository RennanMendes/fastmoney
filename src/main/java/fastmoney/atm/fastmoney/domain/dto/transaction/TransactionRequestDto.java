package fastmoney.atm.fastmoney.domain.dto.transaction;

import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequestDto(
        @NotNull
        BigDecimal value,
        @NotNull
        String pin,
        TransactionType type
) {
}
