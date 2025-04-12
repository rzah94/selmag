package com.github.rzah94.managerapp.repository;

import com.github.rzah94.managerapp.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(Long productId);

    void deleteById(Long id);
}
