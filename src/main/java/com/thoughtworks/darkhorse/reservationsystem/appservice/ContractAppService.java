package com.thoughtworks.darkhorse.reservationsystem.appservice;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.darkhorse.reservationsystem.appservice.exception.ContractNotExistException;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.DepositPaymentRequestRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.*;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.PaymentGateway;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.DepositPaymentRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
public class ContractAppService {

    private final ContractRepository contractRepository;

    private final PaymentGateway paymentGateway;

    private final DepositPaymentRepository depositPaymentRepository;

    @Inject
    public ContractAppService(ContractRepository contractRepository,
                              PaymentGateway paymentGateway,
                              DepositPaymentRepository depositPaymentRepository) {
        this.contractRepository = contractRepository;
        this.paymentGateway = paymentGateway;
        this.depositPaymentRepository = depositPaymentRepository;
    }

    public DepositPaymentRequestRepresentation createDepositPaymentRequest(String contractId, PaymentType paymentType) {
        Optional<Contract> contractOptional = contractRepository.findById(contractId);
        if (contractOptional.isEmpty()) {
            throw new ContractNotExistException(ErrorCode.CONTRACT_NOT_EXIST, ImmutableMap.of("contractId", contractId));
        }

        Transaction unionPayTransaction = paymentGateway.createUnionPayTransaction(Contract.DEPOSIT_AMOUNT);

        DepositPayment depositPayment = new DepositPayment(contractId, unionPayTransaction);

        DepositPayment saved = depositPaymentRepository.save(depositPayment);

        return DepositPaymentRequestRepresentation.from(saved);
    }
}
