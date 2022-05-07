package org.prgrms.awaker.domain.product.category.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateCategoryReqDto(@NotNull UUID categoryId,
                                   @NotBlank String categoryName,
                                   UUID parentId) {
}
