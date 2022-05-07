package org.prgrms.awaker.domain.product.category.dto;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public record NewCategoryReqDto(@NotBlank String categoryName,
                                UUID parentId) {
}
