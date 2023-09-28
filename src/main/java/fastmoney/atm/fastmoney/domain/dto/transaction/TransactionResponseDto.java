package fastmoney.atm.fastmoney.domain.dto.transaction;

import fastmoney.atm.fastmoney.domain.dto.user.UserResponseDto;
import fastmoney.atm.fastmoney.domain.enumerated.FinancialTransaction;
import fastmoney.atm.fastmoney.domain.model.Transaction;

import java.math.BigDecimal;

public record TransactionResponseDto(
        UserResponseDto userDto,
        FinancialTransaction financialTransaction,
        BigDecimal value) {

    public TransactionResponseDto(Transaction transaction) {
        this(
                new UserResponseDto(transaction.getFromAccount()),
                transaction.getFinancialTransaction(),
                transaction.getValue());
    }

}