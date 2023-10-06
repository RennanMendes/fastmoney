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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionServiceTest {

    private static final BigDecimal TRANSACTION_VALUE = BigDecimal.TEN;
    private static final Long ID = 1L;

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository repository;

    @Mock
    MailService mailService;

    @InjectMocks
    private TransactionService transactionService;

    @Autowired
    private List<TransactionValidation> validators;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        transactionService = new TransactionService(userService, repository, validators, mailService);
    }

    @Test
    void shouldReturnTransactionResponseDto_WhenValidDeposit() {
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "1234");
        User user = createUser(BigDecimal.ZERO);
        Transaction transaction = createTransaction(user, FinancialTransaction.DEPOSIT, TransactionType.INPUT);

        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);
        doNothing().when(mailService).depositOrWithdrawEmail(any());
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

        Assertions.assertNotNull(error);
    }

    @Test
    void shouldReturnInvalidPinException_WhenDepositingWithInvalidPin() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");
        User user = createUser(BigDecimal.ZERO);
        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);

        InvalidPinException error =
                Assertions.assertThrows(InvalidPinException.class, () -> transactionService.deposit(ID, request));

        Assertions.assertNotNull(error);
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

        Assertions.assertNotNull(error);
    }

    @Test
    void shouldReturnInvalidPinException_WhenWithdrawingWithInvalidPin() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");
        User user = createUser(new BigDecimal("200"));
        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);

        InvalidPinException error =
                Assertions.assertThrows(InvalidPinException.class, () -> transactionService.withdraw(ID, request));

        Assertions.assertNotNull(error);
    }

    @Test
    void shouldReturnInvalidBalanceException_WhenWithdrawingWithInvalidBalance() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");
        User user = createUser(BigDecimal.ZERO);
        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);

        InvalidBalanceException error =
                Assertions.assertThrows(InvalidBalanceException.class, () -> transactionService.withdraw(ID, request));

        Assertions.assertNotNull(error);
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
        TransactionResponseDto expectedResponse = createTransactionResponse(FinancialTransaction.TRANSFER,
                TransactionType.OUTPUT, response.date(), new BigDecimal("20"));

        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    void shouldReturnInvalidValueException_WhenTransferringWorthlessAmount() {
        Long senderId = 1L;
        Long receiverId = 2L;
        User sender = createUser(new BigDecimal("20"));
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("-100"), "1234");

        when(userService.findByIdAndActiveTrue(senderId)).thenReturn(sender);

        InvalidValueException error =
                Assertions.assertThrows(InvalidValueException.class, () -> transactionService.transfer(senderId, receiverId, request));

        Assertions.assertNotNull(error);
    }

    @Test
    void shouldReturnInvalidPinException_WhenTransferringWithInvalidPin() {
        Long senderId = 1L;
        Long receiverId = 2L;
        User sender = createUser(new BigDecimal("200"));
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");

        when(userService.findByIdAndActiveTrue(senderId)).thenReturn(sender);

        InvalidPinException error =
                Assertions.assertThrows(InvalidPinException.class, () -> transactionService.transfer(senderId, receiverId, request));

        Assertions.assertNotNull(error);
    }

    @Test
    void shouldReturnInvalidBalanceException_WhenTransferringWithInvalidBalance() {
        Long senderId = 1L;
        Long receiverId = 2L;
        User sender = createUser(BigDecimal.ZERO);
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");

        when(userService.findByIdAndActiveTrue(senderId)).thenReturn(sender);

        InvalidBalanceException error =
                Assertions.assertThrows(InvalidBalanceException.class, () -> transactionService.transfer(senderId, receiverId, request));

        Assertions.assertNotNull(error);
    }

    @Test
    void shouldReturnTransactionResponseDto_whenFoundAllByAccountId() {
        Pageable pageable = PageRequest.of(0, 20);
        User user = createUser(new BigDecimal("100"));
        Transaction transaction = createTransaction(user, FinancialTransaction.DEPOSIT, TransactionType.INPUT);
        Page<Transaction> pageTransaction = new PageImpl<>(List.of(transaction));

        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);
        when(repository.findByFromAccountId(user, pageable)).thenReturn(pageTransaction);

        Page<TransactionResponseDto> response = transactionService.statement(ID, pageable);

        TransactionResponseDto transactionResponseDto = createTransactionResponse(FinancialTransaction.DEPOSIT,
                TransactionType.INPUT, response.getContent().get(0).date(), new BigDecimal("100"));
        Page<TransactionResponseDto> pagedExpectedResponse = new PageImpl<>(List.of(transactionResponseDto));

        Assertions.assertEquals(pagedExpectedResponse, response);
    }

    private TransactionResponseDto createTransactionResponse(FinancialTransaction financialTransaction,
                                                             TransactionType type, Instant date, BigDecimal balance) {

        UserTransactionDto receiverDto = financialTransaction.equals(FinancialTransaction.TRANSFER) ? createUserTransactionDto() : null;

        return new TransactionResponseDto(
                financialTransaction,
                type,
                date,
                TRANSACTION_VALUE,
                balance,
                createUserTransactionDto(),
                receiverDto);
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