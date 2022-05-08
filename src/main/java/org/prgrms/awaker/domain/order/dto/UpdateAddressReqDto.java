package org.prgrms.awaker.domain.order.dto;

import java.util.UUID;

public record UpdateAddressReqDto(UUID orderId, String address) {
}
