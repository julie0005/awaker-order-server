package org.prgrms.awaker.domain.product.controller;

import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.dto.NewProductReqDto;
import org.prgrms.awaker.domain.product.dto.ProductResDto;
import org.prgrms.awaker.domain.product.dto.UpdateProductReqDto;
import org.prgrms.awaker.domain.product.service.ProductService;
import org.prgrms.awaker.global.ResponseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ResponseFormat> getProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResDto> productDtos = products.stream().map(ProductResDto::of).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "상품 전체 조회 성공", productDtos));
    }

    @PostMapping
    public ResponseEntity<ResponseFormat> createProduct(@RequestBody NewProductReqDto reqDto) {
        Product newProduct = productService.createProduct(reqDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "상품 생성 성공", ProductResDto.of(newProduct)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseFormat> deleteProduct(@PathVariable UUID productId) {
        Product removedProduct = productService.removeProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "바우처 삭제 성공 : " + productId, ProductResDto.of(removedProduct)));
    }

    @PutMapping
    public ResponseEntity<ResponseFormat> updateProduct(@RequestBody UpdateProductReqDto reqDto) {
        Product updatedProduct = productService.updateProduct(reqDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "바우처 수정 성공 : ", ProductResDto.of(updatedProduct)));
    }
}
