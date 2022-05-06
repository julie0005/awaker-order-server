package org.prgrms.awaker.domain.order;

import org.prgrms.awaker.domain.product.ProductValidator;
import org.prgrms.awaker.global.exception.WrongFormatException;
import org.prgrms.awaker.global.exception.WrongPriceException;

public class OrderItemValidator {
    public static void validatePrice(long totalPrice, long totalDiscount){
        // TODO : 상품 id로 가져온 상품 price * quantity - totalDiscount == toalPrice 인지
        if(totalPrice < 0 || totalDiscount < 0) throw new WrongPriceException("할인 금액과 전체 금액은 0 미만이 될 수 없습니다.");
    }

    public static void validateQuantity(int quantity){
        if(quantity <= 0) throw new WrongFormatException("상품의 주문 수량은 0 이하가 될 수 없습니다.");
    }

}
