package com.thoughtworks.darkhorse.reservationsystem.appservice;

import com.thoughtworks.darkhorse.reservationsystem.appservice.command.CreateProductCommand;
import com.thoughtworks.darkhorse.reservationsystem.appservice.exception.ContractNotExistException;
import com.thoughtworks.darkhorse.reservationsystem.appservice.exception.DepositNotPayedException;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.PageRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductSimpleRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void should_throw_exception_when_create_product_with_non_existed_contract() {
        when(contractRepository.findById(anyString())).thenReturn(Optional.empty());

        CreateProductCommand createProductCommand = new CreateProductCommand("noodle", "delicious", 10.0d, 20);

        ContractNotExistException exception = assertThrows(ContractNotExistException.class,
                () -> productAppService.createProduct("contractId", createProductCommand));

        assertEquals(ErrorCode.CONTRACT_NOT_EXIST, exception.getErrorCode());
    }

    @Test
    void should_list_empty_list_when_list_products_with_negative_page_index() {
        PageRepresentation<ProductSimpleRepresentation> page = productAppService.listProducts(-1, 10);
        assertEquals(0, page.getContents().size());
        assertEquals(0L, page.getTotalSize());
        verify(productRepository, times(0)).findAll(-1, 10);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void should_list_empty_list_when_list_products_with_zero_or_negative_page_size(int pageSize) {
        PageRepresentation<ProductSimpleRepresentation> page = productAppService.listProducts(0, pageSize);
        assertEquals(0, page.getContents().size());
        assertEquals(0L, page.getTotalSize());
        verify(productRepository, times(0)).findAll(0, pageSize);
    }

    @Test
    void should_list_empty_list_when_list_products_with_negative_page_index_and_negative_page_size() {
        PageRepresentation<ProductSimpleRepresentation> page = productAppService.listProducts(-1, -1);
        assertEquals(0, page.getContents().size());
        assertEquals(0L, page.getTotalSize());
        verify(productRepository, times(0)).findAll(-1, -1);
    }
}