package org.prgrms.awaker.domain.product;

import org.prgrms.awaker.global.enums.Boundary;
import org.prgrms.awaker.global.exception.LengthErrorException;
import org.prgrms.awaker.global.exception.WrongPriceException;

public class ProductValidator {
    public static void validateProductName(String productName){
        if(productName.length()> Boundary.MAX_PRODUCT_NAME_LENGTH.getValue()) {
            throw new LengthErrorException("%d자를 초과할 수 없습니다.".formatted(Boundary.MAX_PRODUCT_NAME_LENGTH.getValue()));
        }
    }

    public static void validateDescription(String description){
        if(description.length()> Boundary.MAX_PRODUCT_DESCRIPTION_LENGTH.getValue()) {
            throw new LengthErrorException("%d자를 초과할 수 없습니다.".formatted(Boundary.MAX_PRODUCT_DESCRIPTION_LENGTH.getValue()));
        }
    }

    public static void validatePrice(long price){
        if(price > Boundary.MAX_PRODUCT_PRICE.getValue() || price < Boundary.MIN_PRODUCT_PRICE.getValue()){
            throw new WrongPriceException("유효하지 않은 가격입니다.");
        }
    }

    public static void validateDiscountedPrice(long discountedPrice, long price){
        if(discountedPrice > price){
            throw new WrongPriceException("할인가는 정가를 초과할 수 없습니다.");
        }
        if(discountedPrice == 0){
            throw new WrongPriceException("할인가는 0이 될 수 없습니다.");
        }
    }
}
