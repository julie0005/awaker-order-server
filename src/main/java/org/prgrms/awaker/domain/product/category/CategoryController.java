package org.prgrms.awaker.domain.product.category;

import org.prgrms.awaker.domain.product.category.dto.CategoryResDto;
import org.prgrms.awaker.domain.product.category.dto.NewCategoryReqDto;
import org.prgrms.awaker.domain.product.category.dto.UpdateCategoryReqDto;
import org.prgrms.awaker.global.ResponseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ResponseFormat> getRootCategories() {
        List<Category> categories = categoryService.getRootCategories();
        List<CategoryResDto> categoryResDtos = categories.stream().map(CategoryResDto::of).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "최상위 카테고리 조회 성공", categoryResDtos));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseFormat> getRootCategories(@PathVariable UUID categoryId) {
        List<Category> children = categoryService.getChildrenCategories(categoryId);
        List<CategoryResDto> categoryResDtos = children.stream().map(CategoryResDto::of).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "하위 카테고리 조회 성공", categoryResDtos));
    }

    @PostMapping
    public ResponseEntity<ResponseFormat> createCategory(@RequestBody NewCategoryReqDto reqDto) {
        Category newCategory = categoryService.createCategory(reqDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "카테고리 생성 성공", CategoryResDto.of(newCategory)));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseFormat> deleteCategory(@PathVariable UUID categoryId) {
        Category removed = categoryService.removeCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "카테고리 삭제 성공 : " + categoryId, CategoryResDto.of(removed)));
    }

    @PutMapping
    public ResponseEntity<ResponseFormat> updateCategory(@RequestBody UpdateCategoryReqDto reqDto) {
        Category updated = categoryService.updateCategory(reqDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "카테고리 수정 성공", CategoryResDto.of(updated)));
    }

}
