package com.thoughtworks.darkhorse.reservationsystem.appservice;


import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateProductCommand {
    @NotNull(message = "product name should not be null")
    private String name;

    private String description;

    @PositiveOrZero(message = "product price should not be negative")
    private Double price;

    @PositiveOrZero(message = "product prepare time should not be negative")
    private Integer prepareMinutes;

    public Product toDomainObject() {
        return Product.builder()
                .name(name)
                .description(description)
                .price((int) (price * 100))
                .prepareMinutes(prepareMinutes)
                .build();
    }
}
