package com.thoughtworks.darkhorse.reservationsystem.domainmodel;

import lombok.*;

import java.math.BigInteger;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositPayment {
    private String id;

    private String contractId;

    private String transactionId;

    private BigInteger amount;

    private Instant createdAt;

    private Instant expiredAt;

    private Boolean paid = false;

    public DepositPayment(String contractId, Transaction transaction) {
        this.contractId = contractId;
        this.transactionId = transaction.getId();
        this.amount = transaction.getAmount();
        this.createdAt = transaction.getCreatedAt();
        this.expiredAt = transaction.getExpiredAt();
    }
}
