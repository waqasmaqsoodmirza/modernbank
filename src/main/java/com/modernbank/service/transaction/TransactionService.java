package com.modernbank.service.transaction;

import com.modernbank.domain.Transaction;
import com.modernbank.exceptions.InsufficientFundsException;
import com.modernbank.exceptions.InvalidAccountNumberException;

import java.util.List;

public interface TransactionService {
    boolean transfer(Long senderAccountId, Long receiverAccountId, Double amount) throws InsufficientFundsException;
    List<Transaction> getNumberOfTransactions(Long accountId, int numberOfTransactions) throws InvalidAccountNumberException;
}
