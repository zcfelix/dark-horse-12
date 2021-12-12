package com.thoughtworks.darkhorse.reservationsystem.appservice;

import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAppServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductAppService productAppService;

    @Test
    void should_create_product_success() {
        final Instant now = Instant.now();
        final Contract stubContract = new Contract("contractId", true, 100, now, now);

        when(contractRepository.findById(anyString())).thenReturn(Optional.of(stubContract));
        when(productRepository.save(any(Product.class))).then(args -> {
            Product passed = args.getArgument(0, Product.class);
            passed.setId("productId");
            return passed;
        });

        CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", 10.0d, 20);

        ProductRepresentation representation = productAppService.createProduct("contractId", createProductCommand);

        assertEquals("/products/productId", representation.getUri());
        assertEquals("noodle", representation.getName());
        assertEquals("delicious", representation.getDescription());
        assertEquals(10.0d, representation.getPrice());
        assertEquals(20, representation.getPrepareMinutes());
        assertEquals(10.0d - stubContract.getServiceChargeRate() / 10000.0 * 10.0d, representation.getEarning());
    }

    @Test
    void should_throw_exception_when_create_product_before_deposit_paying() {
        final Instant now = Instant.now();
        final Contract stubContract = new Contract("contractId", false, 100, now, now);

        when(contractRepository.findById(anyString())).thenReturn(Optional.of(stubContract));

        CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", 10.0d, 20);

        DepositNotPayedException exception = assertThrows(DepositNotPayedException.class,
                () -> productAppService.createProduct("contractId", createProductCommand));

        assertEquals(ErrorCode.DEPOSIT_NOT_PAYED, exception.getErrorCode());
    }
}