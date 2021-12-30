package com.thoughtworks.darkhorse.reservationsystem.domainservice;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.PaymentType;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Transaction;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.PaymentGateway;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.UnsupportedDepositPaymentTypeException;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ContractRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
public class ContractService {
    private final ContractRepository contractRepository;
    private final PaymentGateway paymentGateway;

    @Inject
    public ContractService(ContractRepository contractRepository, PaymentGateway paymentGateway) {
        this.contractRepository = contractRepository;
        this.paymentGateway = paymentGateway;
    }

    public Optional<Contract> findContract(String contractId) {
        return contractRepository.findById(contractId);
    }

    public Transaction createDepositPaymentTransaction(String paymentType) {
        Transaction transaction;
        if (paymentType.equalsIgnoreCase(PaymentType.UNION_PAY.toString())) {
            transaction = paymentGateway.createUnionPayTransaction(Contract.DEPOSIT_AMOUNT);
        } else if (paymentType.equalsIgnoreCase(PaymentType.BANK_TRANSFER.toString())) {
            transaction = paymentGateway.createBankTransferTransaction(Contract.DEPOSIT_AMOUNT);
        } else {
            throw new UnsupportedDepositPaymentTypeException(ErrorCode.UNSUPPORTED_DEPOSIT_PAYMENT_TYPE,
                    ImmutableMap.of("supportedType", "UNION_PAY | BANK_TRANSFER"));
        }
        return transaction;
    }
}
