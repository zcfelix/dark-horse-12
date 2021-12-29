package com.thoughtworks.darkhorse.reservationsystem.domainservice.repository;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Product;

import java.util.List;

public interface ProductRepository {
    Product save(Product product);

    Page<Product> findAll(Integer pageIndex, Integer pageSize);

    void deleteAll();

    void saveAllAndFlush(List<Product> products);
}
