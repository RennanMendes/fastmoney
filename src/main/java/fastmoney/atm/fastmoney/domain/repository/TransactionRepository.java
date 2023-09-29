package fastmoney.atm.fastmoney.domain.repository;

import fastmoney.atm.fastmoney.domain.model.Transaction;
import fastmoney.atm.fastmoney.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = :id ORDER BY t.date ASC")
    Page<Transaction> findByFromAccountId(User id, Pageable pageable);

}

