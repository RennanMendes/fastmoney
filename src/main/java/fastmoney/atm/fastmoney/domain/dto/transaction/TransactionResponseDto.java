package fastmoney.atm.fastmoney.domain.dto.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import fastmoney.atm.fastmoney.domain.dto.user.UserTransactionDto;
import fastmoney.atm.fastmoney.domain.enumerated.FinancialTransaction;
import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;
import fastmoney.atm.fastmoney.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionResponseDto(
        FinancialTransaction financialTransaction,
        TransactionType transactionType,
        Instant date,
        BigDecimal value,
        BigDecimal totalAfterOperation,
        UserTransactionDto userDto,
        UserTransactionDto receiverDto) {

    public TransactionResponseDto(Transaction transaction) {
        this(
                transaction.getFinancialTransaction(),
                transaction.getTransactionType(),
                transaction.getDate(),
                transaction.getValue(),
                transaction.getTotalAfterOperation(),
                new UserTransactionDto(transaction.getFromAccount()),
                transaction.getToAccount() != null ? new UserTransactionDto(transaction.getToAccount()) : null);
    }

}