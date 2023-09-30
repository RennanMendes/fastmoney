package fastmoney.atm.fastmoney.domain.service;

import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionRequestDto;
import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionResponseDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserTransactionDto;
import fastmoney.atm.fastmoney.domain.enumerated.FinancialTransaction;
import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;
import fastmoney.atm.fastmoney.domain.exception.InvalidBalanceException;
import fastmoney.atm.fastmoney.domain.exception.InvalidPinException;
import fastmoney.atm.fastmoney.domain.exception.InvalidValueException;
import fastmoney.atm.fastmoney.domain.model.Account;
import fastmoney.atm.fastmoney.domain.model.Transaction;
import fastmoney.atm.fastmoney.domain.model.User;
import fastmoney.atm.fastmoney.domain.repository.TransactionRepository;
import fastmoney.atm.fastmoney.domain.validation.TransactionValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionServiceTest {

    private static final BigDecimal TRANSACTION_VALUE = BigDecimal.TEN;
    private static final Long ID = 1L;

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService transactionService;

    @Autowired
    private List<TransactionValidation> validators;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(userService, repository, validators);
    }

    @Test
    void shouldReturnTransactionResponseDto_WhenValidDeposit() {
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "1234");
        User user = createUser(BigDecimal.ZERO);
        Transaction transaction = createTransaction(user, FinancialTransaction.DEPOSIT, TransactionType.INPUT);

        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);
        when(repository.save(any())).thenReturn(transaction);

        TransactionResponseDto response = transactionService.deposit(ID, request);

        TransactionResponseDto expectedResponse = createTransactionResponse(FinancialTransaction.DEPOSIT,
                TransactionType.INPUT, response.date(), BigDecimal.ZERO);

        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    void shouldReturnInvalidValueException_WhenDepositingWorthlessAmount() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("-100"), "1234");
        User user = createUser(new BigDecimal("20"));
        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);

        InvalidValueException error =
                Assertions.assertThrows(InvalidValueException.class, () -> transactionService.deposit(ID, request));

        Assertions.assertTrue(error instanceof InvalidValueException);
    }

    @Test
    void shouldReturnInvalidPinException_WhenDepositingWithInvalidPin() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");
        User user = createUser(BigDecimal.ZERO);
        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);

        InvalidPinException error =
                Assertions.assertThrows(InvalidPinException.class, () -> transactionService.deposit(ID, request));

        Assertions.assertTrue(error instanceof InvalidPinException);
    }

    @Test
    void shouldReturnTransactionResponseDto_WhenValidWithdrawal() {
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "1234");
        User user = createUser(new BigDecimal("20"));
        Transaction transaction = createTransaction(user, FinancialTransaction.WITHDRAWAL, TransactionType.OUTPUT);

        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);
        when(repository.save(any())).thenReturn(transaction);

        TransactionResponseDto response = transactionService.withdraw(ID, request);
        TransactionResponseDto expectedResponse = createTransactionResponse(FinancialTransaction.WITHDRAWAL,
                TransactionType.OUTPUT, response.date(), new BigDecimal("20"));

        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    void shouldReturnInvalidValueException_WhenToWithdrawWorthlessAmount() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("-100"), "1234");
        User user = createUser(BigDecimal.ZERO);
        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);

        InvalidValueException error =
                Assertions.assertThrows(InvalidValueException.class, () -> transactionService.withdraw(ID, request));

        Assertions.assertTrue(error instanceof InvalidValueException);
    }

    @Test
    void shouldReturnInvalidPinException_WhenWithdrawingWithInvalidPin() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");
        User user = createUser(new BigDecimal("200"));
        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);

        InvalidPinException error =
                Assertions.assertThrows(InvalidPinException.class, () -> transactionService.withdraw(ID, request));

        Assertions.assertTrue(error instanceof InvalidPinException);
    }

    @Test
    void shouldReturnInvalidBalanceException_WhenWithdrawingWithInvalidBalance() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");
        User user = createUser(BigDecimal.ZERO);
        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);

        InvalidBalanceException error =
                Assertions.assertThrows(InvalidBalanceException.class, () -> transactionService.withdraw(ID, request));

        Assertions.assertTrue(error instanceof InvalidBalanceException);
    }

    @Test
    void shouldReturnTransactionResponseDto_WhenValidTransfer() {
        Long senderId = 1L;
        Long receiverId = 2L;
        User sender = createUser(new BigDecimal("20"));
        User receiver = createUser(new BigDecimal("20"));
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "1234");

        Transaction transaction = new Transaction(sender, receiver, FinancialTransaction.TRANSFER,
                TransactionType.OUTPUT, TRANSACTION_VALUE);


        when(userService.findByIdAndActiveTrue(senderId)).thenReturn(sender);
        when(userService.findByIdAndActiveTrue(receiverId)).thenReturn(receiver);
        when(repository.save(any())).thenReturn(transaction);

        TransactionResponseDto response = transactionService.transfer(senderId, receiverId, request);
        TransactionResponseDto expectedResponse = createTransactionTransferResponse(
                response.date(), new BigDecimal("20"));

        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    void shouldReturnInvalidValueException_WhenTransferringWorthlessAmount() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("-100"), "1234");
        Long senderId = 1L;
        User sender = createUser(new BigDecimal("20"));

        when(userService.findByIdAndActiveTrue(senderId)).thenReturn(sender);

        InvalidValueException error =
                Assertions.assertThrows(InvalidValueException.class, () -> transactionService.withdraw(ID, request));

        Assertions.assertTrue(error instanceof InvalidValueException);
    }

    @Test
    void shouldReturnInvalidPinException_WhenTransferringWithInvalidPin() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");
        Long senderId = 1L;
        User sender = createUser(new BigDecimal("200"));

        when(userService.findByIdAndActiveTrue(senderId)).thenReturn(sender);

        InvalidPinException error =
                Assertions.assertThrows(InvalidPinException.class, () -> transactionService.withdraw(ID, request));

        Assertions.assertTrue(error instanceof InvalidPinException);
    }

    @Test
    void shouldReturnInvalidBalanceException_WhenTransferringWithInvalidBalance() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");
        Long senderId = 1L;
        User sender = createUser(BigDecimal.ZERO);

        when(userService.findByIdAndActiveTrue(senderId)).thenReturn(sender);

        InvalidBalanceException error =
                Assertions.assertThrows(InvalidBalanceException.class, () -> transactionService.withdraw(ID, request));

        Assertions.assertTrue(error instanceof InvalidBalanceException);
    }

    private TransactionResponseDto createTransactionResponse(FinancialTransaction financialTransaction,
                                                             TransactionType type, Instant date, BigDecimal balance ) {
        return new TransactionResponseDto(
                financialTransaction,
                type,
                date,
                TRANSACTION_VALUE,
                balance,
                createUserTransactionDto(),
                null);
    }

    private TransactionResponseDto createTransactionTransferResponse(Instant date, BigDecimal balance) {
        return new TransactionResponseDto(
                FinancialTransaction.TRANSFER,
                TransactionType.OUTPUT,
                date,
                TRANSACTION_VALUE,
                balance,
                createUserTransactionDto(),
                createUserTransactionDto());
    }

    private Transaction createTransaction(User user, FinancialTransaction financialTransaction, TransactionType type) {
        return new Transaction(user, financialTransaction, type, TRANSACTION_VALUE);
    }

    private UserTransactionDto createUserTransactionDto() {
        return new UserTransactionDto(createUser(TransactionServiceTest.TRANSACTION_VALUE));
    }

    private User createUser(BigDecimal balance) {
        return new User(ID, "Rennan", "724.622.900-01", "rennan@email.com", "1234", createAccount(balance), true);
    }

    private Account createAccount(BigDecimal balance) {
        return new Account("1010", 2525, balance, "1234");
    }

}