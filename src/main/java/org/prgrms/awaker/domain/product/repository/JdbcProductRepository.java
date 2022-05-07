package org.prgrms.awaker.domain.product.repository;

import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.global.enums.Target;
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

@Repository
public class JdbcProductRepository implements ProductRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcProductRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //TODO : RowMapper 리팩토링
    //TODO : 최하위 카테고리 프린트 방법
    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        UUID productId = toUUID(resultSet.getBytes("product_id"));
        UUID categoryId = toUUID(resultSet.getBytes("category_id"));
        UUID parentCategoryId = toNullableUUID(resultSet, "parent_id");
        logger.info("parentCategoryId : {}",parentCategoryId);

        String productName = resultSet.getString("product_name");
        String categoryName = resultSet.getString("category_name");

        long price = resultSet.getLong("price");
        long discountedPrice = resultSet.getLong("discounted_price");

        Target targetUser = Target.valueOf(resultSet.getString("target"));
        String description = resultSet.getString("description");

        LocalDateTime productCreatedAt = toLocalDateTime(resultSet.getTimestamp("p.created_at"));
        LocalDateTime productUpdatedAt = toLocalDateTime(resultSet.getTimestamp("p.updated_at"));
        LocalDateTime categoryCreatedAt = toLocalDateTime(resultSet.getTimestamp("c.created_at"));
        LocalDateTime categoryUpdatedAt = toLocalDateTime(resultSet.getTimestamp("c.updated_at"));

        Category productCategory = new Category(categoryId, categoryName, parentCategoryId, categoryCreatedAt, categoryUpdatedAt);

        return new Product(productId, productCategory, productName, price, discountedPrice, targetUser, description,  productCreatedAt, productUpdatedAt);
    };

    private Map<String, Object> toParamMap(Product product) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("productId", product.getProductId().toString().getBytes());
        paramMap.put("productName", product.getProductName());
        paramMap.put("categoryId", product.getCategory().getCategoryId().toString().getBytes());
        paramMap.put("target", product.getTargetUser().toString());
        paramMap.put("price", product.getPrice());
        paramMap.put("discountedPrice", product.getDiscountedPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("createdAt", product.getCreatedAt());
        paramMap.put("updatedAt", product.getUpdatedAt());
        return paramMap;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("select * from products p join category c on p.category_id=c.category_id", productRowMapper);
    }

    @Override
    public Product insert(Product product) {
        int update = jdbcTemplate.update("INSERT INTO products(product_id, product_name, category_id, target, price, discounted_price, description, created_at, updated_at)" +
                " VALUES(UUID_TO_BIN(:productId), :productName, UUID_TO_BIN(:categoryId), :target, :price, :discountedPrice, :description, :createdAt, :updatedAt)", toParamMap(product));
        if (update != 1) throw new SqlStatementFailException("Nothing was inserted");
        return product;
    }

    @Override
    public Product update(Product product) {
        int update = jdbcTemplate.update("UPDATE products SET product_name = :productName, category_id = UUID_TO_BIN(:categoryId), target = :target, price = :price, discounted_price = :discountedPrice, description = :description, updated_at = :updatedAt"
                        + " WHERE product_id = UUID_TO_BIN(:productId)",
                toParamMap(product));
        if (update != 1) throw new SqlStatementFailException("Nothing was updated");
        return product;
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM products p JOIN category c on p.category_id = c.category_id WHERE product_id = UUID_TO_BIN(:productId)",
                            Collections.singletonMap("productId", productId.toString().getBytes()), productRowMapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return jdbcTemplate.query(
                """
                        WITH RECURSIVE CTE AS (
                            SELECT category_id, parent_id, category_name, depth
                            FROM category
                            WHERE category_id=UUID_TO_BIN(:categoryId)
                            UNION ALL
                            SELECT c.category_id, c.parent_id, c.category_name, c.depth
                            FROM category c
                            JOIN CTE b ON c.parent_id = b.category_id
                        )
                        select * from products p join category c on p.category_id = c.category_id where exists (SELECT category_id FROM CTE WHERE CTE.category_id = p.category_id)
                        """,
                Collections.singletonMap("categoryId", category.getCategoryId().toString().getBytes()), productRowMapper
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM products", Collections.emptyMap());
    }
}
