package com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Transaction;

import java.math.BigInteger;

public interface PaymentGateway {
    Transaction createUnionPayTransaction(BigInteger amount);

    Transaction createBankTransferTransaction(BigInteger depositAmount);
}
