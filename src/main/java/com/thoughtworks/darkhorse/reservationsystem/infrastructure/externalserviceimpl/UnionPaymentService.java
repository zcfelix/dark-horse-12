package com.thoughtworks.darkhorse.reservationsystem.infrastructure.externalserviceimpl;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class UnionPaymentService {

    public CompletableFuture<ResponseEntity<Transaction>> createTransaction(BigInteger amount) {
        Instant now = Instant.now();
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), amount, now, now.plus(5, ChronoUnit.MINUTES));

        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.CREATED).body(transaction));
    }
}
