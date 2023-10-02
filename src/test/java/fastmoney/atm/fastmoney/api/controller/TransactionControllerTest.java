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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        assertEquals(objectMapper.writeValueAsString(expectedResponse), jsonResponse);
        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenWorthlessDeposit() throws Exception {
        Long id = 1L;
        TransactionRequestDto request = new TransactionRequestDto(null, "1234");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/deposits/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenDepositWithoutPin() throws Exception {
        Long id = 1L;
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/deposits/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
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

        assertEquals(objectMapper.writeValueAsString(expectedResponse), jsonResponse);
        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenWorthlessWithdraw() throws Exception {
        Long id = 1L;
        TransactionRequestDto request = new TransactionRequestDto(null, "1234");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/withdrawals/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenWithdrawWithoutPin() throws Exception {
        Long id = 1L;
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/withdrawals/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus200_WhenValidTransfer() throws Exception {
        Long senderId = 1L;
        Long receiverId = 2L;
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "1234");
        TransactionResponseDto expectedResponse = createTransactionResponse(FinancialTransaction.TRANSFER, TransactionType.OUTPUT);

        when(transactionService.transfer(senderId, receiverId, request)).thenReturn(expectedResponse);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/transfers/{senderId}/{receiverId}", senderId, receiverId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponse), jsonResponse);
        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenWorthlessTransfer() throws Exception {
        Long senderId = 1L;
        Long receiverId = 2L;
        TransactionRequestDto request = new TransactionRequestDto(null, "1234");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/transfers/{senderId}/{receiverId}}", senderId, receiverId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenTransferringWithoutPin() throws Exception {
        Long senderId = 1L;
        Long receiverId = 2L;
        TransactionRequestDto request = new TransactionRequestDto(TRANSACTION_VALUE, "");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/transfers/{senderId}/{receiverId}}", senderId, receiverId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus200_WhenStatement() throws Exception {
        Long id =1L;
        Pageable pageable = Pageable.unpaged();

        TransactionResponseDto responseDto = createTransactionResponse(FinancialTransaction.DEPOSIT,TransactionType.INPUT);
        Page<TransactionResponseDto> pagedResponse = new PageImpl<>(List.of(responseDto));

        when(transactionService.statement(1L,pageable)).thenReturn(pagedResponse);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/{id}", id));

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
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