package fastmoney.atm.fastmoney.domain.service;

import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionResponseDto;
import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionRequestDto;
import fastmoney.atm.fastmoney.domain.dto.transaction.ValidationDto;
import fastmoney.atm.fastmoney.domain.enumerated.FinancialTransaction;
import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;
import fastmoney.atm.fastmoney.domain.model.Transaction;
import fastmoney.atm.fastmoney.domain.model.User;
import fastmoney.atm.fastmoney.domain.repository.TransactionRepository;
import fastmoney.atm.fastmoney.domain.validation.TransactionValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final UserService userService;
    private final TransactionRepository repository;
    private final List<TransactionValidation> validators;

    @Autowired
    public TransactionService(UserService userService, TransactionRepository repository, List<TransactionValidation> validators) {
        this.userService = userService;
        this.repository = repository;
        this.validators = validators;
    }

    public TransactionResponseDto deposit(Long id, TransactionRequestDto requestDto) {
        User user = userService.findByIdAndActiveTrue(id);
        this.validate(user, requestDto, TransactionType.INPUT);
        Transaction transaction = transfer(user, FinancialTransaction.WITHDRAWAL, TransactionType.INPUT, requestDto.value());

        return new TransactionResponseDto(transaction);
    }

    public TransactionResponseDto withdraw(Long id, TransactionRequestDto requestDto) {
        User user = userService.findByIdAndActiveTrue(id);
        this.validate(user, requestDto, TransactionType.OUTPUT);
        Transaction transaction = transfer(user, FinancialTransaction.WITHDRAWAL, TransactionType.OUTPUT, requestDto.value());

        return new TransactionResponseDto(transaction);
    }

    private void validate(User user, TransactionRequestDto requestDto, TransactionType type) {
        ValidationDto dto = new ValidationDto(user, requestDto.value(), requestDto.pin(), type);
        validators.forEach(v -> v.validate(dto));
    }

    private Transaction transfer(User user, FinancialTransaction financialTransaction, TransactionType type, BigDecimal amount) {
        user.getAccount().calculateBalance(type, amount);
        return repository.save(new Transaction(user, financialTransaction, type, amount));
    }

}
