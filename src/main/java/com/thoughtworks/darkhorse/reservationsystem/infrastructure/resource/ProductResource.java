package com.thoughtworks.darkhorse.reservationsystem.infrastructure.resource;

import com.thoughtworks.darkhorse.reservationsystem.appservice.CreateProductCommand;
import com.thoughtworks.darkhorse.reservationsystem.appservice.ProductAppService;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.ProductRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/contracts/{id}/products")
public class ProductResource {

    private final ProductAppService productAppService;

    @Autowired
    public ProductResource(ProductAppService productAppService) {
        this.productAppService = productAppService;
    }

    @PostMapping
    public ResponseEntity<ProductRepresentation> createProduct(@PathVariable("id") String contractId,
                                                               @Valid @RequestBody CreateProductCommand command) {
        ProductRepresentation representation = productAppService.createProduct(contractId, command);
        return ResponseEntity.status(HttpStatus.CREATED).body(representation);
    }
}
