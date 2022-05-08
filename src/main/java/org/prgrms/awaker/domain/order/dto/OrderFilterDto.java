package org.prgrms.awaker.domain.order.dto;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

//Optional을 필드에 사용할 경우 직렬화가 되지 않음. tradeoff 감수하고 클라이언트에서 null 처리할 것.
public record OrderFilterDto(Long priceGreaterThan,
                             Long priceLessThan,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime after,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime before) {
    @Builder
    public OrderFilterDto{

    }
}
