package com.thoughtworks.darkhorse.reservationsystem.domainmodel;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private String id;

    private String name;

    private String description;

    private Integer price;

    private Integer prepareMinutes;

    private Instant cratedAt;

    private Instant updatedAt;
}
