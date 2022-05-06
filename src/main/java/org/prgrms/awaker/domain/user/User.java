package org.prgrms.awaker.domain.user;

import org.prgrms.awaker.global.Utils;
import org.prgrms.awaker.global.enums.Authority;
import org.prgrms.awaker.global.enums.Constant;
import org.prgrms.awaker.global.enums.Gender;
import org.prgrms.awaker.global.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private final UUID userId;
    private String userName;
    private final Gender gender;
    private int age;
    private final String email;
    private String address;
    private String postcode;
    private String password;
    private final Authority auth;
    private UserStatus status;
    private long point;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // TODO : User Login 도메인에서 login하는 지 등 생각
    public User(UUID userId, String userName, Gender gender, int age, String email, String password, Authority auth) {
        UserValidator.validateUserName(userName);
        UserValidator.validateAge(age);
        UserValidator.validateEmail(email);
        UserValidator.validatePassword(password);

        this.userId = userId;
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.password = password;
        this.auth = auth;
        this.point = Constant.INIT_USER_POINT.getValue();
        this.status = UserStatus.NONE;
        this.createdAt = Utils.now();
        this.updatedAt = Utils.now();
    }

    public void changeUserName(String userName) {
        UserValidator.validateUserName(userName);
        this.userName = userName;
        this.updatedAt = Utils.now();
    }

    public void changeAge(int age) {
        UserValidator.validateAge(age);
        this.age = age;
        this.updatedAt = Utils.now();
    }

    public void setAddress(String address) {
        this.address = address;
        this.updatedAt = Utils.now();
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
        this.updatedAt = Utils.now();
    }

    public void changePassword(String password) {
        UserValidator.validatePassword(password);
        this.password = password;
        this.updatedAt = Utils.now();
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
        this.updatedAt = Utils.now();
    }

    public void setPoint(long point) {
        this.point = point;
        this.updatedAt = Utils.now();
    }
}
