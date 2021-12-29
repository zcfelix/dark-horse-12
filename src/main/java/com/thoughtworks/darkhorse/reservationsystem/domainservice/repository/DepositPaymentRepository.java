package com.thoughtworks.darkhorse.reservationsystem.domainservice.repository;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.DepositPayment;

public interface DepositPaymentRepository {
    DepositPayment save(DepositPayment depositPayment);
}
