package com.thoughtworks.darkhorse.reservationsystem.appservice.representation;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductSimpleRepresentation {
    private String uri;

    private String name;

    private String description;

    private Double price;

    private Integer prepareMinutes;

    public static ProductSimpleRepresentation from(Product product) {
        return ProductSimpleRepresentation
                .builder()
                .uri("/products/" + product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice() / 100.0)
                .prepareMinutes(product.getPrepareMinutes())
                .build();
    }
}
