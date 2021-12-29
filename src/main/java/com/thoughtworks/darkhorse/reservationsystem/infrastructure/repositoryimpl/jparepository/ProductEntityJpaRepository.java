package com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.jparepository;

import com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEntityJpaRepository extends JpaRepository<ProductEntity, String> {
}
