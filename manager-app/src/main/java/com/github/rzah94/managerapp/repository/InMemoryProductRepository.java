package com.github.rzah94.managerapp.repository;

import com.github.rzah94.managerapp.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.LongStream;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final List<Product> products = Collections.synchronizedList(new LinkedList<>());

    public InMemoryProductRepository() {
        LongStream.range(1, 4)
                .forEach(i ->
                        this.products.add(new Product(i, "Товар №%d".formatted(i),
                                "Описание товара №%d".formatted(i))));

    }

    @Override
    public List<Product> findAll() {
        return Collections.unmodifiableList(this.products);
    }

    @Override
    public Product save(Product product) {
        product.setId(products.stream()
                              .mapToLong(Product::getId)
                              .max()
                              .orElse(0) + 1
        );
        this.products.add(product);
        return product;
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return this.products.stream()
                .filter(product -> Objects.equals(product.getId(), productId))
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        this.products.removeIf(product -> Objects.equals(id, product.getId()));
    }
}
