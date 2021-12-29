package com.thoughtworks.darkhorse.reservationsystem.domainmodel;

import lombok.*;

import java.math.BigInteger;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private String id;
    private BigInteger amount;
    private Instant createdAt;
    private Instant expiredAt;
}
