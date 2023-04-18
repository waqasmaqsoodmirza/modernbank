package com.modernbank.repository;

import com.modernbank.domain.Account;
import com.modernbank.domain.Transaction;
import com.modernbank.domain.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE (t.senderAccount = ?1 and t.type = ?2) OR" +
            "(t.receiverAccount = ?3 and t.type = ?4) ORDER BY t.dateTime desc")
    List<Transaction> findTransactions(Account senderAccountNo, TransactionType credit, Account receiverAccountNo, TransactionType debit);
}
