package org.prgrms.awaker.domain.product.repository;

import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.category.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findAll();

    Product insert(Product product);

    Product update(Product product);

    Optional<Product> findById(UUID productId);

    List<Product> findByCategory(Category category);

    void deleteById(UUID productId);

    void deleteAll();
}
