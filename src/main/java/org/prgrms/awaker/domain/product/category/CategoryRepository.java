package org.prgrms.awaker.domain.product.category;

import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.dto.ProductResDto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {

    Category insert(Category category);

    Category update(Category category);

    Optional<Category> findById(UUID categoryId);

    List<Category> findByParent(Category category);

    void deleteAll();

}
