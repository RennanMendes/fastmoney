package fastmoney.atm.fastmoney.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionRequestDto;
import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionResponseDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserTransactionDto;
import fastmoney.atm.fastmoney.domain.enumerated.FinancialTransaction;
import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;
import fastmoney.atm.fastmoney.domain.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    private static final String BASE_URL = "/transactions";
    private static final BigDecimal TRANSACTION_VALUE = BigDecimal.TEN;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    public TransactionControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void shouldReturnStatus200_WhenValidDeposit() throws Exception {
        Long id = 1L;
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "1234");
        TransactionResponseDto expectedResponse = createTransactionResponse(FinancialTransaction.DEPOSIT, TransactionType.INPUT);

        when(transactionService.deposit(id, request)).thenReturn(expectedResponse);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/deposits/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(expectedResponse), jsonResponse);
        Assertions.assertTrue(HttpStatus.OK.value() == resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenWorthlessDeposit() throws Exception {
        Long id = 1L;
        TransactionRequestDto request = new TransactionRequestDto(null, "1234");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/deposits/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        Assertions.assertTrue(HttpStatus.BAD_REQUEST.value() == resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenDepositWithoutPin() throws Exception {
        Long id = 1L;
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/deposits/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        Assertions.assertTrue(HttpStatus.BAD_REQUEST.value() == resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus200_WhenValidWithdraw() throws Exception {
        Long id = 1L;
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "1234");
        TransactionResponseDto expectedResponse = createTransactionResponse(FinancialTransaction.WITHDRAWAL, TransactionType.OUTPUT);

        when(transactionService.withdraw(id, request)).thenReturn(expectedResponse);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/withdrawals/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(expectedResponse), jsonResponse);
        Assertions.assertTrue(HttpStatus.OK.value() == resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenWorthlessWithdraw() throws Exception {
        Long id = 1L;
        TransactionRequestDto request = new TransactionRequestDto(null, "1234");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/withdrawals/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        Assertions.assertTrue(HttpStatus.BAD_REQUEST.value() == resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenWithdrawWithoutPin() throws Exception {
        Long id = 1L;
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/withdrawals/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        Assertions.assertTrue(HttpStatus.BAD_REQUEST.value() == resultActions.andReturn().getResponse().getStatus());
    }

    private TransactionResponseDto createTransactionResponse(FinancialTransaction financialTransaction, TransactionType type) {
        return new TransactionResponseDto(
                financialTransaction,
                type,
                Instant.parse("2023-09-29T16:15:19.827691Z"),
                TRANSACTION_VALUE,
                TRANSACTION_VALUE,
                createUserDto(),
                null);
    }

    private UserTransactionDto createUserDto() {
        return new UserTransactionDto(
                1L,
                "Rennan",
                "1010",
                2525);
    }

}