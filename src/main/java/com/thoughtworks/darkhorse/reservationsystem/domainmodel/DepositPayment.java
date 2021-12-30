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

    public static DepositPayment create(String contractId, Transaction transaction) {
        return DepositPayment.builder()
                .contractId(contractId)
                .transactionId(transaction.getId())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt())
                .expiredAt(transaction.getExpiredAt())
                .paid(false)
                .build();
    }
}
