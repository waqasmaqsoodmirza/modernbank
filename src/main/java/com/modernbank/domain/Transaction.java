package com.modernbank.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_account_no")
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_no")
    private Account receiverAccount;

    @Column(name = "amount")
    private double amount;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "transaction_datetime")
    private LocalDateTime dateTime;
}
