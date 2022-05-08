package org.prgrms.awaker.domain.product.service;

import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.domain.product.dto.NewProductReqDto;
import org.prgrms.awaker.domain.product.dto.UpdateProductReqDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    Optional<Product> getProduct(UUID productId);

    List<Product> getProductsByCategory(UUID categoryId);

    List<Product> getAllProducts();

    Product createProduct(NewProductReqDto productReqDto);

    Product removeProduct(UUID productId);

    Product updateProduct(UpdateProductReqDto productReqDto);
}
