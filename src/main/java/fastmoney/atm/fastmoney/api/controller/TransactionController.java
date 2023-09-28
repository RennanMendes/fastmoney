package fastmoney.atm.fastmoney.api.controller;

import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionResponseDto;
import fastmoney.atm.fastmoney.domain.dto.transaction.TransactionRequestDto;
import fastmoney.atm.fastmoney.domain.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/deposits/{id}")
    public ResponseEntity<TransactionResponseDto> deposit(@PathVariable Long id, @RequestBody @Valid TransactionRequestDto requestDto){
        TransactionResponseDto responseDto = transactionService.deposit(id,requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

}
