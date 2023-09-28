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
        ValidationDto dto = new ValidationDto(user, requestDto.value(), requestDto.pin(), TransactionType.INPUT);

        validators.forEach(v -> v.validate(dto));

        user.getAccount().calculateBalance(TransactionType.INPUT, requestDto.value());
        Transaction transaction = transaction(user, FinancialTransaction.DEPOSIT, TransactionType.INPUT, requestDto.value());

        return new TransactionResponseDto(transaction);
    }

    private Transaction save(Transaction transactional) {
        return repository.save(transactional);
    }

    private Transaction transaction(User user, FinancialTransaction financialTransaction, TransactionType type, BigDecimal value) {
        return this.save(new Transaction(user, financialTransaction, type, value));
    }
}
