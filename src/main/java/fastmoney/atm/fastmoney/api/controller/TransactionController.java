package fastmoney.atm.fastmoney.api.controller;

import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionResponseDto;
import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionRequestDto;
import fastmoney.atm.fastmoney.domain.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/deposits/{id}")
    public ResponseEntity<TransactionResponseDto> deposit(@PathVariable Long id, @RequestBody @Valid TransactionRequestDto requestDto) {
        TransactionResponseDto responseDto = transactionService.deposit(id, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/withdrawals/{id}")
    public ResponseEntity<TransactionResponseDto> withdraw(@PathVariable Long id, @RequestBody @Valid TransactionRequestDto requestDto) {
        TransactionResponseDto responseDto = transactionService.withdraw(id, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/transfers/{senderId}/{receiverId}")
    public ResponseEntity<TransactionResponseDto> transfer(@PathVariable Long senderId, @PathVariable Long receiverId,
                                                           @RequestBody @Valid TransactionRequestDto requestDto) {
        TransactionResponseDto responseDto = transactionService.transfer(senderId, receiverId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<TransactionResponseDto>> statement(@PathVariable Long id, Pageable page) {
        Page<TransactionResponseDto> responseDto = transactionService.statement(id, page);
        return ResponseEntity.ok().body(responseDto);
    }

}
