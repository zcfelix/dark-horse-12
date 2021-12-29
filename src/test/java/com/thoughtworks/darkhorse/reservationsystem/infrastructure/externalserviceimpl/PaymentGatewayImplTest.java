package com.thoughtworks.darkhorse.reservationsystem.infrastructure.externalserviceimpl;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Transaction;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.CreateTransactionTimeoutException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentGatewayImplTest {

    @Mock
    private UnionPaymentService unionPaymentService;

    @InjectMocks
    private PaymentGatewayImpl paymentGateway;

    @Test
    void should_create_transaction_success_when_union_pay_create_transaction_success() {
        final Instant now = Instant.now();
        final Transaction stubTransaction = new Transaction("transactionId",
                BigInteger.valueOf(300),
                now,
                now.plus(5, ChronoUnit.MINUTES));
        when(unionPaymentService.createTransaction(any(BigInteger.class))).thenReturn(
                CompletableFuture.completedFuture(ResponseEntity.status(201).body(stubTransaction)));

        Transaction transaction = paymentGateway.createUnionPayTransaction(BigInteger.valueOf(300));

        assertEquals(stubTransaction.getId(), transaction.getId());
        assertEquals(stubTransaction.getAmount(), transaction.getAmount());
        assertEquals(stubTransaction.getCreatedAt(), transaction.getCreatedAt());
        assertEquals(stubTransaction.getExpiredAt(), transaction.getExpiredAt());
    }

    @Test
    void should_throw_time_out_exception_when_union_pay_create_transaction_time_out() {
        when(unionPaymentService.createTransaction(any(BigInteger.class))).thenReturn(
                CompletableFuture.failedFuture(new TimeoutException()));

        CreateTransactionTimeoutException exception = assertThrows(CreateTransactionTimeoutException.class,
                () -> paymentGateway.createUnionPayTransaction(BigInteger.valueOf(300)));

        assertEquals(ErrorCode.CREATE_TRANSACTION_TIME_OUT, exception.getErrorCode());
    }
}