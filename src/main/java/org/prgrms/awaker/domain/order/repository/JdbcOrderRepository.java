package org.prgrms.awaker.domain.order.repository;

import org.prgrms.awaker.domain.order.Order;
import org.prgrms.awaker.domain.order.OrderItem;
import org.prgrms.awaker.domain.order.dto.OrderFilterDto;
import org.prgrms.awaker.domain.user.User;
import org.prgrms.awaker.global.enums.Authority;
import org.prgrms.awaker.global.enums.Gender;
import org.prgrms.awaker.global.enums.OrderStatus;
import org.prgrms.awaker.global.enums.UserStatus;
import org.prgrms.awaker.global.exception.SqlStatementFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

import static org.prgrms.awaker.global.Utils.*;
import static org.prgrms.awaker.global.Utils.toLocalDateTime;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcOrderRepository.class);


    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<OrderItem> orderItemRowMapper = (resultSet, i) -> {
        UUID productId = toUUID(resultSet.getBytes("product_id"));
        long productTotalPrice = resultSet.getLong("product_total_price");
        long productTotalDiscount = resultSet.getLong("product_total_discount");
        int quantity = resultSet.getInt("quantity");

        return OrderItem.builder()
                .productId(productId)
                .productTotalPrice(productTotalPrice)
                .productTotalDiscount(productTotalDiscount)
                .quantity(quantity)
                .build();
    };


    private final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        UUID orderId = toUUID(resultSet.getBytes("order_id"));
        long totalPrice = resultSet.getLong("total_price");
        long totalDiscount = resultSet.getLong("total_discount");
        OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        String address = resultSet.getString("o.address");
        String postcode = resultSet.getString("o.postcode");
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("o.created_at"));
        LocalDateTime updatdAt = toLocalDateTime(resultSet.getTimestamp("o.updated_at"));
        List<OrderItem> orderItems = findOrderItemsByOrderId(orderId);

        UUID userId = toUUID(resultSet.getBytes("user_id"));
        String userEmail = resultSet.getString("email");
        String userName = resultSet.getString("user_name");
        String password = resultSet.getString("password");
        int age = resultSet.getInt("age");
        UserStatus status = UserStatus.valueOf(resultSet.getString("status"));
        Gender gender = Gender.valueOf(resultSet.getString("gender"));
        String userAddress = resultSet.getString("address");
        String userPostcode = resultSet.getString("postcode");
        Authority authority = Authority.valueOf(resultSet.getString("authority"));
        int point = resultSet.getInt("point");
        LocalDateTime userCreatedAt = toLocalDateTime(resultSet.getTimestamp("u.created_at"));
        LocalDateTime userUpdatedAt = toLocalDateTime(resultSet.getTimestamp("u.updated_at"));

        User user = User.builder()
                .userId(userId)
                .userName(userName)
                .email(userEmail)
                .age(age)
                .auth(authority)
                .gender(gender)
                .password(password)
                .createdAt(userCreatedAt)
                .updatedAt(userUpdatedAt)
                .build();
        user.setAddress(userAddress);
        user.setPoint(point);
        user.setPostcode(userPostcode);
        user.setStatus(status);

        Order order = Order.builder()
                .orderId(orderId)
                .user(user)
                .totalPrice(totalPrice)
                .totalDiscount(totalDiscount)
                .address(address)
                .postcode(postcode)
                .orderItems(orderItems)
                .createdAt(createdAt)
                .updatedAt(updatdAt)
                .build();
        order.setOrderStatus(orderStatus);
        return order;
    };

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("userId", order.getUser().getUserId().toString().getBytes());
        paramMap.put("totalPrice", order.getTotalPrice());
        paramMap.put("totalDiscount", order.getTotalDiscount());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, OrderItem item, LocalDateTime createdAt, LocalDateTime updatedAt) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.getProductId().toString().getBytes());
        paramMap.put("productTotalPrice", item.getProductTotalPrice());
        paramMap.put("productTotalDiscount", item.getProductTotalDiscount());
        paramMap.put("quantity", item.getQuantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);
        return paramMap;
    }

    private List<OrderItem> findOrderItemsByOrderId(UUID orderId) {
        return jdbcTemplate.query("select * from order_items where order_id = UUID_TO_BIN(:orderId)",
                Collections.singletonMap("orderId", orderId.toString().getBytes()), orderItemRowMapper);
    }

    @Override
    public Order insert(Order order) {
        jdbcTemplate.update("INSERT INTO orders(order_id, user_id, total_price, total_discount, address, postcode, order_status, created_at, updated_at) " +
                "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:userId), :totalPrice, :totalDiscount, :address, :postcode, :orderStatus, :createdAt, :updatedAt)", toOrderParamMap(order));
        order.getOrderItems().forEach(item ->
                jdbcTemplate.update("INSERT INTO order_items(order_id, product_id, product_total_price, product_total_discount, quantity, created_at, updated_at) " +
                                "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :productTotalPrice, :productTotalDiscount, :quantity, :createdAt, :updatedAt)",
                        toOrderItemParamMap(order.getOrderId(), item, order.getCreatedAt(), order.getUpdatedAt())));
        return order;
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM orders o join users u on o.user_id = u.user_id WHERE order_id = UUID_TO_BIN(:orderId)",
                            Collections.singletonMap("orderId", orderId.toString().getBytes()), orderRowMapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findAll(OrderSortMethod sortMethod) {
        if (sortMethod == null) {
            sortMethod = OrderSortMethod.RECENT;
        }
        String sql = "SELECT * FROM orders o join users u on o.user_id = u.user_id order by %s".formatted(sortMethod.getQuery());
        return jdbcTemplate.query(sql, orderRowMapper);
    }

    @Override
    public List<Order> findByFilter(OrderFilterDto filter, OrderSortMethod sortMethod) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM orders o join users u on o.user_id = u.user_id WHERE ");
        Map<String, Object> paramMap = new HashMap<>();
        boolean isFirstFilter = true;
        if (filter.priceGreaterThan() != null) {
            isFirstFilter = false;
            queryBuilder.append("total_price >= :minTotalPrice ");
            paramMap.put("minTotalPrice", filter.priceGreaterThan());
        }
        if (filter.priceLessThan() != null) {
            if (!isFirstFilter) {
                queryBuilder.append("and ");
            }
            isFirstFilter = false;
            queryBuilder.append("total_price <= :maxTotalPrice ");
            paramMap.put("maxTotalPrice", filter.priceLessThan());
        }
        if (filter.after() != null) {
            if (!isFirstFilter) {
                queryBuilder.append("and ");
            }
            isFirstFilter = false;
            queryBuilder.append("created_at >= str_to_date(:after, \"%Y-%m-%d\") ");
            paramMap.put("after", filter.after().toString());
        }
        if (filter.before() != null) {
            if (!isFirstFilter) {
                queryBuilder.append("and ");
            }
            queryBuilder.append("created_at <= str_to_date(:before, \"%Y-%m-%d\") ");
            paramMap.put("after", filter.before().toString());
        }
        logger.info("sql : {}", queryBuilder.toString());
        return jdbcTemplate.query(queryBuilder.toString(), paramMap, orderRowMapper);
    }

    @Override
    public Order update(Order order) {
        int update = jdbcTemplate.update("UPDATE orders SET user_id = UUID_TO_BIN(:userId), " +
                        "address = :address, " +
                        "postcode = :postcode, order_status = :orderStatus, updated_at = :updatedAt"
                        + " WHERE order_id = UUID_TO_BIN(:orderId)",
                toOrderParamMap(order));
        if (update != 1) throw new SqlStatementFailException("Nothing was updated");
        return order;
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from orders", Collections.emptyMap());
        jdbcTemplate.update("delete from order_items", Collections.emptyMap());
    }
}
