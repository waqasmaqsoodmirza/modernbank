package com.modernbank.rest;

import com.modernbank.domain.Account;
import com.modernbank.domain.Status;
import com.modernbank.domain.Transaction;
import com.modernbank.domain.TransferRequest;
import com.modernbank.exceptions.InsufficientFundsException;
import com.modernbank.exceptions.InvalidAccountNumberException;
import com.modernbank.service.account.AccountService;
import com.modernbank.service.transaction.TransactionService;
import com.modernbank.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountResource {
    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @GetMapping("{accountId}/balance")
    public ResponseEntity<?> getAccountBalance(@PathVariable Long accountId) {
        try {
            return ResponseEntity.ok().body(accountService.getAccount(accountId));
        } catch (InvalidAccountNumberException invalidAccountNumberException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Status(invalidAccountNumberException.getMessage()));
        }
    }
    @PostMapping("")
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account) {
        if(accountService.accountExists(account.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Status("Account Already Exists"));
        }
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        if(!accountService.accountExists(accountId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Status("Account does not exist"));
        }
        Account account = accountService.getAccount(accountId);
        accountService.deleteAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }
    @GetMapping("{accountId}/statements/mini")
    public ResponseEntity<?> getAccountStatement(@PathVariable Long accountId) {
        try {
            List<Transaction> transactions = transactionService.getNumberOfTransactions(accountId, 20);
            String output = ResponseUtil.getTransactionsAsString(transactions);
            return ResponseEntity.ok().body(output);
        } catch (InvalidAccountNumberException invalidAccountNumberException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Status(invalidAccountNumberException.getMessage()));
        }
    }
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest request) {
        try {
            transactionService.transfer(request.getSenderAccountId(), request.getReceiverAccountId(), request.getAmount());
        } catch (InsufficientFundsException insufficientFundsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Status(insufficientFundsException.getMessage()));
        } catch (InvalidAccountNumberException invalidAccountNumberException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Status(invalidAccountNumberException.getMessage()));
        }
        return ResponseEntity.ok().body(new Status("OK"));
    }
}
