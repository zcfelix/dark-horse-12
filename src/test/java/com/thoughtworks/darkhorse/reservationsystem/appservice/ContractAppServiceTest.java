package com.thoughtworks.darkhorse.reservationsystem.appservice;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.darkhorse.reservationsystem.appservice.exception.ContractNotExistException;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.DepositPaymentRequestRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.*;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.CreateTransactionTimeoutException;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.PaymentGateway;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.DepositPaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractAppServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private DepositPaymentRepository depositPaymentRepository;

    @InjectMocks
    private ContractAppService contractAppService;

    @Test
    void should_create_deposit_payment_request_success() {
        final Instant now = Instant.now();
        final Contract stubContract = new Contract("contractId", false, 100, now, now);
        when(contractRepository.findById(anyString())).thenReturn(Optional.of(stubContract));

        final Transaction stubTransaction = new Transaction("transactionId",
                Contract.DEPOSIT_AMOUNT,
                now.plus(5, ChronoUnit.MINUTES),
                now.plus(10, ChronoUnit.MINUTES));
        when(paymentGateway.createUnionPayTransaction(any(BigInteger.class))).thenReturn(stubTransaction);

        when(depositPaymentRepository.save(any(DepositPayment.class))).then(args -> {
            DepositPayment passed = args.getArgument(0, DepositPayment.class);
            passed.setId("paymentId");
            return passed;
        });

        DepositPaymentRequestRepresentation representation = contractAppService.createDepositPaymentRequest("contractId", PaymentType.UNION_PAY);

        assertEquals("/contracts/contractId/deposit-payments/paymentId", representation.getUri());
        assertEquals(Contract.DEPOSIT_AMOUNT.doubleValue(), representation.getAmount());
        assertEquals(now.plus(5, ChronoUnit.MINUTES), representation.getCreatedAt());
        assertEquals(now.plus(10, ChronoUnit.MINUTES), representation.getExpiredAt());
    }

    @Test
    void should_throw_exception_when_payment_request_time_out() {
        final Instant now = Instant.now();
        final Contract stubContract = new Contract("contractId", false, 100, now, now);
        when(contractRepository.findById(anyString())).thenReturn(Optional.of(stubContract));
        doThrow(new CreateTransactionTimeoutException(ErrorCode.CREATE_TRANSACTION_TIME_OUT, ImmutableMap.of()))
                .when(paymentGateway).createUnionPayTransaction(any(BigInteger.class));

        assertThrows(CreateTransactionTimeoutException.class,
                () -> contractAppService.createDepositPaymentRequest("contractId", PaymentType.UNION_PAY));

        verify(depositPaymentRepository, times(0)).save(any(DepositPayment.class));
    }

    @Test
    void should_throw_exception_when_create_payment_request_with_non_existed_contract() {
        when(contractRepository.findById(anyString())).thenReturn(Optional.empty());

        ContractNotExistException exception = assertThrows(ContractNotExistException.class,
                () -> contractAppService.createDepositPaymentRequest("contractId", PaymentType.UNION_PAY));

        assertEquals(ErrorCode.CONTRACT_NOT_EXIST, exception.getErrorCode());
    }
}