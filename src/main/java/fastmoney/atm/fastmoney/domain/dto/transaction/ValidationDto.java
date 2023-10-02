package fastmoney.atm.fastmoney.domain.dto.transaction;

import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;
import fastmoney.atm.fastmoney.domain.model.User;

import java.math.BigDecimal;

public record ValidationDto(
        User user,
        BigDecimal value,
        String pin,
        TransactionType type) {
}
