package org.prgrms.awaker.domain.order;

import org.prgrms.awaker.domain.user.UserValidator;
import org.prgrms.awaker.global.exception.WrongPriceException;

// TODO : userId가 존재하는지
// TODO : totalPrice가 orderItems에서 계산한 합계와 같은지
// TODO : totalDiscount가 orderItems의 prodcutTotalDiscount 합계와 같은지
public class OrderValidator {
    public static void validateTotalPrice(long totalPrice) {
        if (totalPrice <= 0) throw new WrongPriceException("전체 가격의 합계는 0 이하가 될 수 없습니다.");
    }

    public static void validateTotalDiscount(long totalDiscount) {
        if (totalDiscount <= 0 ) throw new WrongPriceException("전체 할인 가격은 0 이하가 될 수 없습니다.");
    }

    public static void validateAddress(String address){
        UserValidator.validateAddress(address);
    }

    public static void validatePostcode(String postcode){
        UserValidator.validatePostcode(postcode);
    }
}
