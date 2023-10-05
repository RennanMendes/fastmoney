package fastmoney.atm.fastmoney.domain.dto.mail;

import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;

import java.math.BigDecimal;

public record EmailDto(
        String name,
        BigDecimal amount,
        String to,
        TransactionType type
) {
}
