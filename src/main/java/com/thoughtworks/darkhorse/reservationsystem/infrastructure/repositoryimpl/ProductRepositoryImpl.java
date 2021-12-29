package com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.Page;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ProductRepository;
import com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.entity.ProductEntity;
import com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.jparepository.ProductEntityJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductEntityJpaRepository entityRepository;

    @Autowired
    public ProductRepositoryImpl(ProductEntityJpaRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Product save(Product product) {
        return entityRepository.save(ProductEntity.from(product)).toDomainObject();
    }

    @Override
    public Page<Product> findAll(Integer pageIndex, Integer pageSize) {
        org.springframework.data.domain.Page<ProductEntity> entities = entityRepository.findAll(PageRequest.of(pageIndex, pageSize));

        return new Page<>(
                entities.getContent().stream().map(ProductEntity::toDomainObject).collect(toList()),
                entities.getTotalElements()
        );
    }

    @Override
    public void deleteAll() {
        entityRepository.deleteAll();
    }

    @Override
    public void saveAllAndFlush(List<Product> products) {
        entityRepository.saveAllAndFlush(products.stream().map(ProductEntity::from).collect(toList()));
    }

}
