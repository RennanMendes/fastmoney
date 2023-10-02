package fastmoney.atm.fastmoney.domain.model;

import fastmoney.atm.fastmoney.domain.enumerated.FinancialTransaction;
import fastmoney.atm.fastmoney.domain.enumerated.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "tb_statement")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FinancialTransaction financialTransaction;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date")
    private Instant date;

    @Column(name = "transaction_value")
    private BigDecimal value;
    private BigDecimal totalAfterOperation;

    @ManyToOne()
    @JoinColumn()
    private User fromAccount;

    @ManyToOne()
    @JoinColumn()
    private User toAccount;

    public Transaction(User user, FinancialTransaction financialTransaction, TransactionType transactionType, BigDecimal value) {
        this.financialTransaction = financialTransaction;
        this.transactionType = transactionType;
        this.date = Instant.now();
        this.value = value;
        this.totalAfterOperation = user.getAccount().getBalance();
        this.fromAccount = user;
    }

    public Transaction(User fromAccount, User toAccount, FinancialTransaction financialTransaction, TransactionType transactionType, BigDecimal value) {
        this.financialTransaction = financialTransaction;
        this.transactionType = transactionType;
        this.date = Instant.now();
        this.value = value;
        this.totalAfterOperation = fromAccount.getAccount().getBalance();
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }


}
