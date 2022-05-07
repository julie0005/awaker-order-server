package org.prgrms.awaker.domain.product.category;

import org.prgrms.awaker.domain.product.repository.JdbcProductRepository;
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
public class JdbcCategoryRepository implements CategoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCategoryRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcCategoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Category> categoryRowMapper = (resultSet, i) -> {
        UUID categoryId = toUUID(resultSet.getBytes("category_id"));
        UUID parentId = toNullableUUID(resultSet, "parent_id");
        String categoryName = resultSet.getString("category_name");
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return Category.builder()
                .categoryId(categoryId)
                .parentId(parentId)
                .categoryName(categoryName)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    };

    private Map<String, Object> toParamMap(Category category){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("categoryId", category.getCategoryId().toString().getBytes());
        paramMap.put("categoryName", category.getCategoryName());
        paramMap.put("parentId", category.getParentId() == null ? null : category.getParentId().toString().getBytes());
        paramMap.put("createdAt", category.getCreatedAt());
        paramMap.put("updatedAt", category.getUpdatedAt());
        return paramMap;
    }


    @Override
    public Category insert(Category category) {
        String sql;

        if(category.getParentId() == null){
            sql = "INSERT INTO category(category_id, category_name, created_at, updated_at) VALUES(UUID_TO_BIN(:categoryId), :categoryName, :createdAt, :updatedAt)";
        }
        else{
            sql = "INSERT INTO category(category_id, category_name, parent_id, depth, created_at, updated_at) VALUES(UUID_TO_BIN(:categoryId), :categoryName, UUID_TO_BIN(:parentId), (SELECT tmp.depth FROM category tmp WHERE category_id = UUID_TO_BIN(:parentId))+1,  :createdAt, :updatedAt)";
        }
        logger.info("Category Insert SQL : {}",sql);
        logger.info("paramMap : {}", toParamMap(category));
        logger.info("categoryId : {}", category.getCategoryId());
        int update = jdbcTemplate.update(sql, toParamMap(category));
        if(update!=1) throw new SqlStatementFailException("Nothing was inserted");
        return category;
    }

    @Override
    public Category update(Category category) {
        int update = jdbcTemplate.update("UPDATE category SET category_name = :categoryName, parent_id = UUID_TO_BIN(:parentId), updated_at = :updatedAt"
                        + " WHERE category_id = UUID_TO_BIN(:categoryId)",
                toParamMap(category));
        if(update!=1) throw new SqlStatementFailException("Nothing was updated");
        return category;
    }

    @Override
    public Optional<Category> findById(UUID categoryId) {
        try{
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM category WHERE category_id = UUID_TO_BIN(:categoryId)",
                            Collections.singletonMap("categoryId", categoryId.toString().getBytes()), categoryRowMapper
                    )
            );
        }catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Category> findByParent(Category category) {
        return jdbcTemplate.query(
                """
                        WITH RECURSIVE CTE AS (
                            SELECT category_id, parent_id, category_name, depth, created_at, updated_at
                            FROM category
                            WHERE category_id=UUID_TO_BIN(:categoryId)
                            UNION ALL
                            SELECT c.category_id, c.parent_id, c.category_name, c.depth, c.created_at, c.updated_at
                            FROM category c
                            JOIN CTE b ON c.parent_id = b.category_id
                        )
                        select * from CTE order by depth;
                        """,
                Collections.singletonMap("categoryId", category.getCategoryId().toString().getBytes()), categoryRowMapper
        );
    }

    @Override
    public void deleteById(UUID categoryId) {
        int delete = jdbcTemplate.update(
                """
                        WITH RECURSIVE CTE AS (
                            SELECT category_id, parent_id
                            FROM category
                            WHERE category_id=UUID_TO_BIN(:categoryId)
                            UNION ALL
                            SELECT c.category_id, c.parent_id
                            FROM category c
                            JOIN CTE b ON c.parent_id = b.category_id
                        )
                        delete from category where exists (select category_id from CTE where category.category_id=CTE.category_id);
                        """,
                 Collections.singletonMap("categoryId", categoryId.toString().getBytes()));
        if (delete < 0) throw new SqlStatementFailException("정상적으로 삭제되지 않았습니다.");
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM category", Collections.emptyMap());
    }
}
