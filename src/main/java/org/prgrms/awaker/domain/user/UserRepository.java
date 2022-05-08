package org.prgrms.awaker.domain.user;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User insert(User order);

    Optional<User> findById(UUID orderId);
}
