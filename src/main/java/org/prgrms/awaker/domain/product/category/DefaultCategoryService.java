package org.prgrms.awaker.domain.product.category;

import org.prgrms.awaker.domain.product.category.dto.NewCategoryReqDto;
import org.prgrms.awaker.domain.product.category.dto.UpdateCategoryReqDto;
import org.prgrms.awaker.global.Utils;
import org.prgrms.awaker.global.exception.UnavailableActionException;
import org.prgrms.awaker.global.exception.UnknownException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public DefaultCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> getProduct(UUID categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public List<Category> getRootCategories() {
        return categoryRepository.findRoots();
    }


    @Override
    public List<Category> getChildrenCategories(UUID parentCategory) {
        categoryRepository.findById(parentCategory).orElseThrow(() -> new UnknownException("존재하지 않는 카테고리입니다."));
        return categoryRepository.findByParentId(parentCategory);
    }

    @Override
    public Category createCategory(NewCategoryReqDto categoryReqDto) {
        if(categoryReqDto.parentId() != null){
            categoryRepository.findById(categoryReqDto.parentId()).orElseThrow(() -> new UnknownException("존재하지 않는 카테고리입니다."));
        }
        Category newCategory = Category.builder()
                .categoryId(UUID.randomUUID())
                .categoryName(categoryReqDto.categoryName())
                .parentId(categoryReqDto.parentId())
                .createdAt(Utils.now())
                .updatedAt(Utils.now())
                .build();
        return categoryRepository.insert(newCategory);
    }

    @Override
    public Category removeCategory(UUID categoryId) {
        Category oldCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new UnknownException("존재하지 않는 카테고리입니다."));
        categoryRepository.deleteById(categoryId);
        return oldCategory;
    }

    @Override
    public Category updateCategory(UpdateCategoryReqDto categoryReqDto) {
        Category origin = categoryRepository.findById(categoryReqDto.categoryId()).orElseThrow(() -> new UnknownException("존재하지 않는 카테고리입니다."));
        if(categoryReqDto.parentId() != null){
            categoryRepository.findById(categoryReqDto.parentId()).orElseThrow(() -> new UnknownException("부모 카테고리가 존재하지 않습니다."));
            origin.setParentId(categoryReqDto.parentId());
        }
        origin.setCategoryName(categoryReqDto.categoryName());
        return categoryRepository.update(origin);
    }
}
