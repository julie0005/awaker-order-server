package org.prgrms.awaker.domain.product.service;

import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.domain.product.category.CategoryRepository;
import org.prgrms.awaker.domain.product.dto.NewProductReqDto;
import org.prgrms.awaker.domain.product.dto.UpdateProductReqDto;
import org.prgrms.awaker.domain.product.repository.ProductRepository;
import org.prgrms.awaker.global.Utils;
import org.prgrms.awaker.global.exception.UnknownException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultProductService implements  ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DefaultProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Product> getProduct(UUID productId) {
        return productRepository.findById(productId);
    }

    @Override
    public List<Product> getProductsByCategory(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // TODO : Dto, Entity Validation 중복
    @Override
    public Product createProduct(NewProductReqDto productReqDto) {
        Category productCategory = categoryRepository.findById(productReqDto.categoryId()).orElseThrow(() -> new UnknownException("존재하지 않는 카테고리입니다."));
        Product newProduct = Product.builder()
                .productId(UUID.randomUUID())
                .productName(productReqDto.productName())
                .targetUser(productReqDto.targetUser())
                .description(productReqDto.description())
                .category(productCategory)
                .price(productReqDto.price())
                .discountedPrice(productReqDto.discountedPrice())
                .createdAt(Utils.now())
                .updatedAt(Utils.now())
                .build();
        return productRepository.insert(newProduct);
    }

    @Override
    public Product removeProduct(UUID productId) {
        Product oldProduct = productRepository.findById(productId).orElseThrow(() -> new UnknownException("존재하지 않는 상품입니다."));
        productRepository.deleteById(productId);
        return oldProduct;
    }

    @Override
    public Product updateProduct(UpdateProductReqDto productReqDto) {
        Product product = productRepository.findById(productReqDto.productId()).orElseThrow(() -> new UnknownException("존재하지 않는 상품입니다."));
        Category productCategory = categoryRepository.findById(productReqDto.categoryId()).orElseThrow(() -> new UnknownException("존재하지 않는 카테고리입니다."));

        product.setProductName(productReqDto.productName());
        product.setDescription(productReqDto.description());
        product.setPrice(productReqDto.price());
        product.setDiscountedPrice(productReqDto.discountedPrice());
        product.setTargetUser(productReqDto.targetUser());
        product.setCategory(productCategory);

        return productRepository.update(product);
    }
}
