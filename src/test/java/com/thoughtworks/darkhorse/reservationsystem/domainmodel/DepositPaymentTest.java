package com.thoughtworks.darkhorse.reservationsystem.domainmodel;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DepositPaymentTest {

    @Test
    void should_create_deposit_payment_success() {
        final Instant now = Instant.now();
        final Contract dummyContract = new Contract("contractId", false, 100, now, now);

        final Transaction dummyTransaction = new Transaction("transactionId",
                BigInteger.valueOf(300),
                now,
                now.plus(5, ChronoUnit.MINUTES));

        DepositPayment depositPayment = DepositPayment.create(dummyContract.getId(), dummyTransaction);

        assertEquals(dummyContract.getId(), depositPayment.getContractId());
        assertEquals(dummyTransaction.getId(), depositPayment.getTransactionId());
        assertEquals(dummyTransaction.getCreatedAt(), depositPayment.getCreatedAt());
        assertEquals(dummyTransaction.getExpiredAt(), depositPayment.getExpiredAt());
        assertFalse(depositPayment.getPaid());
    }
}