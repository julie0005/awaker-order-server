package org.prgrms.awaker.domain.product.category;

import org.prgrms.awaker.global.enums.Boundary;
import org.prgrms.awaker.global.exception.LengthErrorException;

public class CategoryValidator {
    public static void validateCategoryName(String categoryName){
        if(categoryName.length()> Boundary.MAX_CATEGORY_NAME_LENGTH.getValue()) {
            throw new LengthErrorException("%d자를 초과할 수 없습니다.".formatted(Boundary.MAX_CATEGORY_NAME_LENGTH.getValue()));
        }
    }
}
