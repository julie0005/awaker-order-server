package org.prgrms.awaker.domain.user;

import org.prgrms.awaker.domain.user.dto.UserResDto;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User insert(User order);

    Optional<UserResDto> findById(UUID orderId);
}
