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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
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

    @Transactional
    public TransactionResponseDto deposit(Long id, TransactionRequestDto requestDto) {
        User user = userService.findByIdAndActiveTrue(id);
        this.validate(user, requestDto, TransactionType.INPUT);
        Transaction transaction = performTransaction(user, FinancialTransaction.DEPOSIT, TransactionType.INPUT, requestDto.value());

        return new TransactionResponseDto(transaction);
    }

    @Transactional
    public TransactionResponseDto withdraw(Long id, TransactionRequestDto requestDto) {
        User user = userService.findByIdAndActiveTrue(id);
        this.validate(user, requestDto, TransactionType.OUTPUT);
        Transaction transaction = performTransaction(user, FinancialTransaction.WITHDRAWAL, TransactionType.OUTPUT, requestDto.value());

        return new TransactionResponseDto(transaction);
    }

    @Transactional
    public TransactionResponseDto transfer(Long senderId, Long receiverId, TransactionRequestDto requestDto) {
        User sender = userService.findByIdAndActiveTrue(senderId);
        User receiver = userService.findByIdAndActiveTrue(receiverId);

        this.validate(sender, requestDto, TransactionType.OUTPUT);
        Transaction transaction = this.performTransfer(sender, receiver, requestDto.value());

        return new TransactionResponseDto(transaction);
    }

    public Page<TransactionResponseDto> statement(Long id, Pageable page) {
        User user = userService.findByIdAndActiveTrue(id);
        Page<Transaction> transactions = repository.findByFromAccountId(user, page);
        return transactions.map(TransactionResponseDto::new);
    }

    private void validate(User user, TransactionRequestDto requestDto, TransactionType type) {
        ValidationDto dto = new ValidationDto(user, requestDto.value(), requestDto.pin(), type, LocalTime.now());
        validators.forEach(v -> v.validate(dto));
    }

    private Transaction performTransaction(User user, FinancialTransaction financialTransaction, TransactionType type, BigDecimal amount) {
        user.getAccount().calculateBalance(type, amount);
        return repository.save(new Transaction(user, financialTransaction, type, amount));
    }

    private Transaction performTransfer(User sender, User receiver, BigDecimal amount) {
        sender.getAccount().calculateBalance(TransactionType.OUTPUT, amount);
        receiver.getAccount().calculateBalance(TransactionType.INPUT, amount);
        repository.save(new Transaction(receiver, sender, FinancialTransaction.TRANSFER, TransactionType.INPUT, amount));
        return repository.save(new Transaction(sender, receiver, FinancialTransaction.TRANSFER, TransactionType.OUTPUT, amount));
    }
}