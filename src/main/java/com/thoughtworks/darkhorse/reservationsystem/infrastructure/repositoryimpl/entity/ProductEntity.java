package com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.entity;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class ProductEntity {
    @Id
    @GenericGenerator(name = "id_generator", strategy = "uuid")
    @GeneratedValue(generator = "id_generator")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "prepareMinutes")
    private Integer prepareMinutes;

    @Column(name = "created_at", updatable = false)
    @Generated(GenerationTime.ALWAYS)
    private Instant cratedAt;

    @Column(name = "updated_at")
    @Generated(GenerationTime.ALWAYS)
    private Instant updatedAt;

    public static Product toDomainObject(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .prepareMinutes(entity.getPrepareMinutes())
                .cratedAt(entity.getCratedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static ProductEntity from(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .prepareMinutes(product.getPrepareMinutes())
                .cratedAt(product.getCratedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
