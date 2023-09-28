package fastmoney.atm.fastmoney.domain.model;

import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Random;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Account {
    @Column(name = "account_branch")
    private String branch;

    @Column(name = "account_number")
    private int number;

    @Column(name = "account_balance")
    private BigDecimal balance;

    private String pin;

    public Account(String branch, String pin) {
        this.branch = branch;
        this.number = newAccountNumber();
        this.balance = BigDecimal.ZERO;
        this.pin = pin;
    }

    private int newAccountNumber() {
        Random random = new Random();
        return random.nextInt(9000) + 1000;
    }

    public void update(String pin) {
        this.pin = pin;
    }

    public void calculateBalance(TransactionType transactionType, BigDecimal value) {
        if (transactionType == TransactionType.INPUT) {
            this.balance = this.balance.add(value);
        } else {
            this.balance = this.balance.subtract(value);
        }
    }

}
