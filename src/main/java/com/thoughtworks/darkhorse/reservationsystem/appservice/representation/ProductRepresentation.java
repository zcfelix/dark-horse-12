package com.thoughtworks.darkhorse.reservationsystem.appservice.representation;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductRepresentation {
    private String uri;

    private String name;

    private String description;

    private Double price;

    private Integer prepareMinutes;

    private Double earning;


    public static ProductRepresentation from(Contract contract, Product product) {
        return ProductRepresentation
                .builder()
                .uri("/products/" + product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice() / 100.0)
                .prepareMinutes(product.getPrepareMinutes())
                .earning((product.getPrice() * (1 - contract.getServiceChargeRate() / 10000.0)) / 100.0)
                .build();
    }
}
