package org.prgrms.awaker.domain.product.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {

    Category insert(Category category);

    Category update(Category category);

    Optional<Category> findById(UUID categoryId);

    List<Category> findByParentId(UUID categoryId);

    List<Category> findRoots();

    void deleteById(UUID categoryId);

    void deleteAll();

}
