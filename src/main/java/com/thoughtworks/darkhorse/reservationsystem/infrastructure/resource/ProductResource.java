package com.thoughtworks.darkhorse.reservationsystem.infrastructure.resource;

import com.thoughtworks.darkhorse.reservationsystem.appservice.command.CreateProductCommand;
import com.thoughtworks.darkhorse.reservationsystem.appservice.ProductAppService;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.PageRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductSimpleRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
public class ProductResource {

    private final ProductAppService productAppService;

    @Inject
    public ProductResource(ProductAppService productAppService) {
        this.productAppService = productAppService;
    }

    @PostMapping(path = "/contracts/{id}/products")
    public ResponseEntity<ProductRepresentation> createProduct(@PathVariable("id") String contractId,
                                                               @Valid @RequestBody CreateProductCommand command) {
        ProductRepresentation representation = productAppService.createProduct(contractId, command);
        return ResponseEntity.status(HttpStatus.CREATED).body(representation);
    }

    @GetMapping(path = "/products")
    public ResponseEntity<PageRepresentation<ProductSimpleRepresentation>> listProducts(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        PageRepresentation<ProductSimpleRepresentation> pageRepresentation = productAppService.listProducts(pageIndex, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(pageRepresentation);
    }
}
