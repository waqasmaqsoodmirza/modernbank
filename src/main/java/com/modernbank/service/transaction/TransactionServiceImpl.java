package com.modernbank.service.transaction;

import com.modernbank.domain.Account;
import com.modernbank.domain.Transaction;
import com.modernbank.domain.TransactionType;
import com.modernbank.exceptions.InsufficientFundsException;
import com.modernbank.exceptions.InvalidAccountNumberException;
import com.modernbank.repository.TransactionRepository;
import com.modernbank.service.account.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    @Transactional
    public boolean transfer(Long senderAccountId, Long receiverAccountId, Double amount) throws InsufficientFundsException {
        Account senderAccount = accountService.getAccount(senderAccountId);
        Account receiverAccount = accountService.getAccount(receiverAccountId);
        accountService.withdraw(senderAccount, amount);
        accountService.deposit(receiverAccount, amount);
        debit(receiverAccount, senderAccount, amount);
        credit(receiverAccount, senderAccount, amount);
        return true;
    }

    @Override
    public List<Transaction> getNumberOfTransactions(Long accountId, int numberOfTransactions) throws InvalidAccountNumberException {
        Account account = accountService.getAccount(accountId);
        //return transactionRepository.findBySenderAccountOrReceiverAccountOrderByDateTimeDesc(account, account).stream().limit(numberOfTransactions).collect(Collectors.toList());
        return transactionRepository.findTransactions(account, TransactionType.CREDIT, account, TransactionType.DEBIT);
    }

    private void debit(Account receiver, Account sender, Double amount) {
       Transaction transaction = new Transaction();
       transaction.setAmount(amount);
       transaction.setType(TransactionType.DEBIT);
       transaction.setSenderAccount(sender);
       transaction.setReceiverAccount(receiver);
       transaction.setDateTime(LocalDateTime.now());
       transactionRepository.save(transaction);
    }
    private void credit(Account receiver, Account sender, Double amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(-amount);
        transaction.setType(TransactionType.CREDIT);
        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(receiver);
        transaction.setDateTime(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}
