package fastmoney.atm.fastmoney.domain.dto.account;

import fastmoney.atm.fastmoney.domain.model.Account;

import java.math.BigDecimal;

public record AccountResponseDto(
        String branch,
        int number,
        BigDecimal balance) {

    public AccountResponseDto(Account account) {
        this(account.getBranch(), account.getNumber(), account.getBalance());
    }

}
