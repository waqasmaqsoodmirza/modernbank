package com.modernbank.service.account;

import com.modernbank.domain.Account;
import com.modernbank.exceptions.InsufficientFundsException;
import com.modernbank.exceptions.InvalidAccountNumberException;
import com.modernbank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public Double getAccountBalance(Long accountId) {
        Account account = getAccount(accountId);
        return account.getBalance();
    }

    @Override
    public Account getAccount(Long accountId) throws InvalidAccountNumberException {
        Optional<Account> account = accountRepository.findById(accountId);
        if(!account.isPresent()) {
            throw new InvalidAccountNumberException("Invalid Account Number");
        }
        return account.get();
    }

    @Override
    public boolean accountExists(Long accountId) {
        return accountRepository.existsById(accountId);
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }


    @Override
    public void withdraw(Account account, Double amount) throws InsufficientFundsException {
        if(amount > account.getBalance()) {
            throw new InsufficientFundsException("Insufficent Funds");
        }
        Double remainingBalance = account.getBalance() - amount;
        account.setBalance(remainingBalance);
        accountRepository.save(account);
    }

    @Override
    public void deposit(Account account, Double amount) {
        if(amount > 0) {
            Double newBalance = account.getBalance() + amount;
            account.setBalance(newBalance);
            accountRepository.save(account);
        }
    }

}
