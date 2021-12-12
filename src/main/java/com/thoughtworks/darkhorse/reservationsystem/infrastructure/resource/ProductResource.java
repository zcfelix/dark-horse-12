package com.thoughtworks.darkhorse.reservationsystem.infrastructure.resource;

import com.thoughtworks.darkhorse.reservationsystem.appservice.CreateProductCommand;
import com.thoughtworks.darkhorse.reservationsystem.appservice.ProductAppService;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductSimpleRepresentation;
import com.thoughtworks.darkhorse.reservationsystem.infrastructure.PageResponse;
import org.flywaydb.core.internal.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductResource {

    private final ProductAppService productAppService;

    @Autowired
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
    public ResponseEntity<PageResponse<ProductSimpleRepresentation>> listProducts(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pair<List<ProductSimpleRepresentation>, Long> pair = productAppService.listProducts(pageIndex, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(new PageResponse<>(pair.getLeft(), pair.getRight()));
    }
}
