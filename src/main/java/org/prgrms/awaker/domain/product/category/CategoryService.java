package org.prgrms.awaker.domain.product.category;

import org.prgrms.awaker.domain.product.category.dto.NewCategoryReqDto;
import org.prgrms.awaker.domain.product.category.dto.UpdateCategoryReqDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Optional<Category> getCategory(UUID categoryId);

    List<Category> getRootCategories();

    List<Category> getChildrenCategories(UUID parentCategory);

    Category createCategory(NewCategoryReqDto categoryReqDto);

    Category removeCategory(UUID categoryId);

    Category updateCategory(UpdateCategoryReqDto categoryReqDto);

}
