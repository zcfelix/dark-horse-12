package com.thoughtworks.darkhorse.reservationsystem.infrastructure.externalserviceimpl;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Transaction;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.CreateTransactionFailedException;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.CreateTransactionTimeoutException;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.PaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class PaymentGatewayImpl implements PaymentGateway {

    private static final Long TIME_OUT_LIMIT = 3L;

    private final UnionPaymentService unionPaymentService;
    private final BankTransferService bankTransferService;

    @Autowired
    public PaymentGatewayImpl(UnionPaymentService unionPaymentService, BankTransferService bankTransferService) {
        this.unionPaymentService = unionPaymentService;
        this.bankTransferService = bankTransferService;
    }

    @Override
    public Transaction createUnionPayTransaction(BigInteger amount) {
        return getTransaction(unionPaymentService.createTransaction(amount));
    }

    @Override
    public Transaction createBankTransferTransaction(BigInteger amount) {
        return getTransaction(bankTransferService.createTransaction(amount));
    }

    private Transaction getTransaction(CompletableFuture<ResponseEntity<Transaction>> transaction) {
        try {
            ResponseEntity<Transaction> entity = transaction.get(TIME_OUT_LIMIT, TimeUnit.SECONDS);
            if (entity.getStatusCode().equals(HttpStatus.CREATED)) {
                return entity.getBody();
            } else {
                throw new CreateTransactionFailedException(ErrorCode.CREATE_TRANSACTION_FAILED,
                        ImmutableMap.of("3rd response status", entity.getStatusCode().toString()));
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new CreateTransactionTimeoutException(ErrorCode.CREATE_TRANSACTION_TIME_OUT,
                    ImmutableMap.of("message", "timeout when call union pay service"));
        }
    }
}
