package com.thoughtworks.darkhorse.reservationsystem.domainservice;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.PaymentType;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Transaction;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.PaymentGateway;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.UnsupportedDepositPaymentTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private ContractService contractService;

    @Test
    void should_create_deposit_payment_transaction_success() {
        Instant now = Instant.now();
        final Transaction stubTransaction = new Transaction("transactionId",
                Contract.DEPOSIT_AMOUNT,
                now.plus(5, ChronoUnit.MINUTES),
                now.plus(10, ChronoUnit.MINUTES));
        when(paymentGateway.createUnionPayTransaction(any(BigInteger.class))).thenReturn(stubTransaction);

        Transaction transaction = contractService.createDepositPaymentTransaction(PaymentType.UNION_PAY.toString());
        assertEquals(stubTransaction.getId(), transaction.getId());
        assertEquals(stubTransaction.getAmount(), transaction.getAmount());
        assertEquals(stubTransaction.getCreatedAt(), transaction.getCreatedAt());
        assertEquals(stubTransaction.getExpiredAt(), transaction.getExpiredAt());
    }

    @Test
    void should_throw_exception_when_create_payment_request_with_unsupported_payment_type() {
        UnsupportedDepositPaymentTypeException exception = assertThrows(UnsupportedDepositPaymentTypeException.class,
                () -> contractService.createDepositPaymentTransaction("Alipay"));
        assertEquals(ErrorCode.UNSUPPORTED_DEPOSIT_PAYMENT_TYPE, exception.getErrorCode());
        verify(paymentGateway, times(0)).createUnionPayTransaction(any());
        verify(paymentGateway, times(0)).createBankTransferTransaction(any());
    }
}