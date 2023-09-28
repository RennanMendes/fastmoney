package fastmoney.atm.fastmoney.domain.repository;

import fastmoney.atm.fastmoney.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
