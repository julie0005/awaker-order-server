package org.prgrms.awaker.domain.product.service;

import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.category.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    List<Product> getProductsByCategory(Category category);

    List<Product> getAllProducts();

    Product createProduct(String productName, Category category, long price);

    Product createProduct(String productName, Category category, long price, String description);
}
