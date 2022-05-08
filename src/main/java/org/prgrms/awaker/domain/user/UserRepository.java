package org.prgrms.awaker.domain.user;

import org.prgrms.awaker.domain.user.dto.UserSqlDto;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User insert(User order);

    Optional<User> findById(UUID orderId);
}
