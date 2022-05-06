package org.prgrms.awaker.domain.cart;

import java.time.LocalDateTime;
import java.util.UUID;

public record Cart(UUID cartId, UUID userId, UUID productId,
                   LocalDateTime createdAt) {
}
