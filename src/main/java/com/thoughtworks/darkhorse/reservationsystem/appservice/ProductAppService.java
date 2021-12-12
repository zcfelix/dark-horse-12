package com.thoughtworks.darkhorse.reservationsystem.appservice;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductAppService {

    private final ContractRepository contractRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductAppService(ContractRepository contractRepository, ProductRepository productRepository) {
        this.contractRepository = contractRepository;
        this.productRepository = productRepository;
    }

    public ProductRepresentation createProduct(String contractId, CreateProductCommand createProductCommand) {
        return contractRepository.findById(contractId).map(c -> {
            if (c.getDepositPayed()) {
                return ProductRepresentation.from(c, productRepository.save(createProductCommand.toDomainObject()));
            }
            throw new DepositNotPayedException(ErrorCode.DEPOSIT_NOT_PAYED, ImmutableMap.of("contractId", contractId));
        }).orElseThrow(
                () -> new ContractNotExistException(
                        ErrorCode.CONTRACT_NOT_EXIST, ImmutableMap.of("contractId", contractId)
                )
        );
    }
}
