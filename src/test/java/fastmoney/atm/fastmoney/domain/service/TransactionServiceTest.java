package fastmoney.atm.fastmoney.domain.service;

import fastmoney.atm.fastmoney.domain.dto.account.AccountResponseDto;
import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionRequestDto;
import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionResponseDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserResponseDto;
import fastmoney.atm.fastmoney.domain.enumerated.FinancialTransaction;
import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionServiceTest {

    private static final String BASE_URL = "/transactions";
    private static final BigDecimal TRANSACTION_VALUE = BigDecimal.TEN;
    private static final Long ID = 1L;

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService transactionService;

    @Autowired
    private  List<TransactionValidation> validators;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(userService, repository, validators);
    }


    @Test
    void shouldReturnTransactionResponseDto_WhenValidDeposit() {
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "1234");
        TransactionResponseDto expectedResponse = createTransactionResponse();
        User user = createUser(BigDecimal.ZERO);
        Transaction transaction = createTransaction(user,FinancialTransaction.DEPOSIT, TransactionType.INPUT);

        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);
        when(repository.save(any())).thenReturn(transaction);

        TransactionResponseDto response = transactionService.deposit(ID,request);

        Assertions.assertEquals(expectedResponse,response);
    }

    @Test
    void shouldReturnInvalidValueException_WhenDepositingWorthlessAmount() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("-100"), "1234");
        User user = createUser(BigDecimal.ZERO);
        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);

        InvalidValueException error =
                Assertions.assertThrows(InvalidValueException.class, () -> {
                    transactionService.deposit(ID,request);
                });

        Assertions.assertTrue(error instanceof InvalidValueException);
    }

    @Test
    void shouldReturnInvalidPinException_WhenDepositingWithInvalidPin() {
        TransactionRequestDto request = new TransactionRequestDto(new BigDecimal("100"), "1235");
        User user = createUser(BigDecimal.ZERO);
        when(userService.findByIdAndActiveTrue(ID)).thenReturn(user);

        InvalidPinException error =
                Assertions.assertThrows(InvalidPinException.class, () -> {
                    transactionService.deposit(ID,request);
                });

        Assertions.assertTrue(error instanceof InvalidPinException);
    }


    private TransactionResponseDto createTransactionResponse() {
        return new TransactionResponseDto(createUserDto(TRANSACTION_VALUE), FinancialTransaction.DEPOSIT, TRANSACTION_VALUE);
    }

    private Transaction createTransaction(User user, FinancialTransaction financialTransaction, TransactionType type){
        return new Transaction(user,financialTransaction,type,TRANSACTION_VALUE);
    }

    private UserResponseDto createUserDto(BigDecimal balance) {
        return new UserResponseDto(createUser(balance));
    }

    private User createUser(BigDecimal balance) {
        return new User(ID, "Rennan", "724.622.900-01", "rennan@email.com", "1234", createAccount(balance), true);
    }

    private Account createAccount(BigDecimal balance) {
        return new Account("1010", 2525, balance, "1234");
    }

}