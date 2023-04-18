package com.modernbank.service.account;

import com.modernbank.domain.Account;
import com.modernbank.exceptions.InsufficientFundsException;
import com.modernbank.exceptions.InvalidAccountNumberException;

public interface AccountService {
    Double getAccountBalance(Long accountId);
    Account getAccount(Long accountId) throws InvalidAccountNumberException;
    boolean accountExists(Long accountId);
    Account createAccount(Account account);
    void deleteAccount(Account account);
    void withdraw(Account account, Double amount) throws InsufficientFundsException;
    void deposit(Account account, Double amount);
}
