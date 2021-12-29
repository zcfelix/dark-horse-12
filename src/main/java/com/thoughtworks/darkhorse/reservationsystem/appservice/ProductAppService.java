package com.thoughtworks.darkhorse.reservationsystem.appservice;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.darkhorse.reservationsystem.appservice.command.CreateProductCommand;
import com.thoughtworks.darkhorse.reservationsystem.appservice.exception.ContractNotExistException;
import com.thoughtworks.darkhorse.reservationsystem.appservice.exception.DepositNotPayedException;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.PageRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductSimpleRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.Page;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ProductRepository;

import javax.inject.Inject;
import javax.inject.Named;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Named
public class ProductAppService {

    private final ContractRepository contractRepository;
    private final ProductRepository productRepository;

    @Inject
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

    public PageRepresentation<ProductSimpleRepresentation> listProducts(Integer pageIndex, Integer pageSize) {
        if (pageIndex < 0 || pageSize <= 0) {
            return new PageRepresentation<>(emptyList(), 0L);
        }
        Page<Product> page = productRepository.findAll(pageIndex, pageSize);
        return new PageRepresentation<>(
                page.getElements().stream().map(ProductSimpleRepresentation::from).collect(toList()),
                page.getTotalSize()
        );
    }
}
