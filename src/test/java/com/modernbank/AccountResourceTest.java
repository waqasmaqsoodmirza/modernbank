package com.modernbank;

import com.modernbank.domain.Account;
import com.modernbank.domain.Status;
import com.modernbank.domain.TransferRequest;
import com.modernbank.service.account.AccountService;
import com.modernbank.service.transaction.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    //1.
    @Test
    public void testValidTransfer() throws Exception {
        // Given
        Account senderAccount = accountService.getAccount(11L);
        Account receiverAccount = accountService.getAccount(22L);

        TransferRequest request = new TransferRequest();
        request.setSenderAccountId(senderAccount.getId());
        request.setReceiverAccountId(receiverAccount.getId());
        request.setAmount(Double.valueOf(10));

        // When
        ResponseEntity<?> responseEntity = restTemplate.postForEntity("/accounts/transfer", request, Object.class);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Account updatedSenderAccount = accountService.getAccount(senderAccount.getId());
        Account updatedReceiverAccount = accountService.getAccount(receiverAccount.getId());

        assertEquals(Double.valueOf(90), updatedSenderAccount.getBalance());
        assertEquals(Double.valueOf(110), updatedReceiverAccount.getBalance());
    }
    //2.Invalid Account, positive funds
    @Test
    public void testInValidAccountTransfer() throws Exception {
        //Given
        TransferRequest request = new TransferRequest();
        request.setSenderAccountId(11L);
        request.setReceiverAccountId(33333L);
        request.setAmount(Double.valueOf(10));
        // When
        ResponseEntity<?> responseEntity = restTemplate.postForEntity("/accounts/transfer", request, Object.class);
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    //3. valid details no funds
    @Test
    public void testValidAccountInsufficientFundsTransfer() throws Exception {
        //Given
        TransferRequest request = new TransferRequest();
        request.setSenderAccountId(11L);
        request.setReceiverAccountId(33L);
        request.setAmount(Double.valueOf(110));
        // When
        ResponseEntity<?> responseEntity = restTemplate.postForEntity("/accounts/transfer", request, Status.class);
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Insufficent Funds", ((Status) responseEntity.getBody()).getMessage());
    }
    //4. valid account balance
    @Test
    public void testValidAccountBalance() throws Exception {
        //Given
        String validAccount = "/accounts/11/balance";
        // When
        ResponseEntity<?> responseEntity = restTemplate.getForEntity(validAccount, Account.class);
        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Double.valueOf(90), ((Account) responseEntity.getBody()).getBalance());
    }
    //5. mini statement
    @Test
    public void testMiniStatement() {
        //Given
        String validAccount = "/accounts/11/statements/mini";
        // When
        ResponseEntity<?> responseEntity = restTemplate.getForEntity(validAccount, List.class);
        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, ((ArrayList)responseEntity.getBody()).size());
    }
    //6. check balance for invalid account
    @Test
    public void testInValidAccountBalance() throws Exception {
        //Given
        String validAccount = "/accounts/11111/balance";
        // When
        ResponseEntity<?> responseEntity = restTemplate.getForEntity(validAccount, Status.class);
        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Invalid Account Number", ((Status) responseEntity.getBody()).getMessage());
    }
    //7. mini statement for invalid account
    @Test
    public void testInvalidAccountMiniStatement() {
        //Given
        String validAccount = "/accounts/11111/statements/mini";
        // When
        ResponseEntity<?> responseEntity = restTemplate.getForEntity(validAccount, Status.class);
        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Invalid Account Number", ((Status) responseEntity.getBody()).getMessage());
    }
}
