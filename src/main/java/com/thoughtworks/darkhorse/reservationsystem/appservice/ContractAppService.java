package com.thoughtworks.darkhorse.reservationsystem.appservice;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.darkhorse.reservationsystem.appservice.exception.ContractNotExistException;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.DepositPaymentRequestRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.DepositPayment;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Transaction;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.ContractService;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.DepositPaymentRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
public class ContractAppService {

    private final ContractService contractService;
    private final DepositPaymentRepository depositPaymentRepository;

    @Inject
    public ContractAppService(ContractService contractService, DepositPaymentRepository depositPaymentRepository) {
        this.depositPaymentRepository = depositPaymentRepository;
        this.contractService = contractService;
    }

    public DepositPaymentRequestRepresentation createDepositPaymentRequest(String contractId, String paymentType) {
        Optional<Contract> contractOptional = contractService.findContract(contractId);
        if (contractOptional.isEmpty()) {
            throw new ContractNotExistException(ErrorCode.CONTRACT_NOT_EXIST, ImmutableMap.of("contractId", contractId));
        }

        Transaction transaction = contractService.createDepositPaymentTransaction(paymentType);
        DepositPayment saved = depositPaymentRepository.save(DepositPayment.create(contractId, transaction));
        return DepositPaymentRequestRepresentation.from(saved);
    }
}
