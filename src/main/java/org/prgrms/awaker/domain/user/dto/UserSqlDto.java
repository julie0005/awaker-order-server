package org.prgrms.awaker.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import org.prgrms.awaker.domain.user.UserValidator;
import org.prgrms.awaker.global.enums.Authority;
import org.prgrms.awaker.global.enums.Gender;
import org.prgrms.awaker.global.enums.UserStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class UserSqlDto {
    @NotNull
    private final UUID userId;
    @NotNull
    private final String userName;
    @NotNull
    private final Gender gender;
    private final int age;
    @NotNull
    private final String email;
    private final String address;
    private final String postcode;
    @NotNull
    private final Authority auth;
    @NotNull
    private final UserStatus status;
    private final long point;
    @NotNull
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public UserSqlDto(UUID userId, String userName, String address, String postcode, Gender gender, int age, String email, Authority auth, LocalDateTime createdAt, LocalDateTime updatedAt, int point, UserStatus status) {
        UserValidator.validateUserName(userName);
        UserValidator.validateAge(age);
        UserValidator.validateEmail(email);
        UserValidator.validateAddress(address);
        UserValidator.validatePostcode(postcode);

        this.userId = userId;
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.auth = auth;
        this.point = point;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
