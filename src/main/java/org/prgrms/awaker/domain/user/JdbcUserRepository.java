package org.prgrms.awaker.domain.user;

import org.prgrms.awaker.global.Utils;
import org.prgrms.awaker.global.enums.Authority;
import org.prgrms.awaker.global.enums.Gender;
import org.prgrms.awaker.global.enums.UserStatus;
import org.prgrms.awaker.global.exception.SqlStatementFailException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

import static org.prgrms.awaker.global.Utils.toLocalDateTime;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final RowMapper<User> userRowMapper = (resultSet, rowNum) -> {
        UUID userId = Utils.toUUID(resultSet.getBytes("user_id"));
        String name = resultSet.getString("user_name");
        String email = resultSet.getString("email");
        UserStatus status = UserStatus.valueOf(resultSet.getString("status"));
        Gender gender = Gender.valueOf(resultSet.getString("gender"));
        String address = resultSet.getString("address");
        String postcode = resultSet.getString("postcode");
        String password = resultSet.getString("password");
        Authority authority = Authority.valueOf(resultSet.getString("authority"));
        int point = resultSet.getInt("point");
        int age = resultSet.getInt("age");
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        User user = User.builder()
                .userId(userId)
                .userName(name)
                .email(email)
                .age(age)
                .auth(authority)
                .gender(gender)
                .password(password)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        user.setAddress(address);
        user.setPostcode(postcode);
        user.setPoint(point);
        user.setStatus(status);
        return user;
    };

    public JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Map<String, Object> toParamMap(User user) {
        return new HashMap<>() {{
            put("userId", user.getUserId().toString().getBytes());
            put("userName", user.getUserName());
            put("email", user.getEmail());
            put("status", user.getStatus().toString());
            put("age", user.getAge());
            put("authority", user.getAuth().toString());
            put("address", user.getAddress());
            put("postcode", user.getPostcode());
            put("point", user.getPoint());
            put("createdAt", user.getCreatedAt());
            put("updatedAt", user.getUpdatedAt());
            put("gender", user.getGender().toString());
            put("password", user.getPassword());
        }};
    }


    @Override
    public User insert(User user) {
        int update = jdbcTemplate.update("INSERT INTO users(user_id, user_name, gender, age, email, address, postcode, password, authority, created_at, updated_at)" +
                " VALUES(UUID_TO_BIN(:userId), :userName, :gender, :age, :email, :address, :postcode, :password, :authority, :createdAt, :updatedAt)", toParamMap(user));
        if (update != 1) throw new SqlStatementFailException("Nothing was inserted");
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = UUID_TO_BIN(:userId)",
                            Collections.singletonMap("userId", userId.toString().getBytes()), userRowMapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
