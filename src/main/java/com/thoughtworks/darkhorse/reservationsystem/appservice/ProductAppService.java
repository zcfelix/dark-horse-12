package com.thoughtworks.darkhorse.reservationsystem.appservice;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductSimpleRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.ProductRepository;
import org.flywaydb.core.internal.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

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

    public Pair<List<ProductSimpleRepresentation>, Long> listProducts(Integer pageIndex, Integer pageSize) {
        if (pageIndex < 0 || pageSize <= 0) {
            return Pair.of(ImmutableList.of(), 0L);
        }
        Page<Product> page = productRepository
                .findAll(PageRequest.of(pageIndex, pageSize));
        return Pair.of(
                page.getContent().stream().map(ProductSimpleRepresentation::from).collect(toList()),
                page.getTotalElements()
        );
    }
}
