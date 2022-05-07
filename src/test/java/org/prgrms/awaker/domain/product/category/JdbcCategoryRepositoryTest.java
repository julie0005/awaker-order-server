package org.prgrms.awaker.domain.product.category;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import org.junit.jupiter.api.*;
import org.prgrms.awaker.global.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcCategoryRepositoryTest {

    static EmbeddedMysql embeddedMysql;

    private final Category rootCategory = Category.builder()
            .categoryId(UUID.randomUUID())
            .categoryName("상의")
            .createdAt(Utils.now())
            .updatedAt(Utils.now())
            .build();
    private final Category firstSubCategory = Category.builder()
            .categoryId(UUID.randomUUID())
            .categoryName("티셔츠")
            .parentId(rootCategory.getCategoryId())
            .createdAt(Utils.now())
            .updatedAt(Utils.now())
            .build();

    @BeforeAll
    void setUp() {
        MysqldConfig config = aMysqldConfig(v8_0_11)
                .withCharset(Charset.UTF8)
                .withPort(2214)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();
        embeddedMysql = anEmbeddedMysql(config)
                .addSchema("test-awaker_db", ScriptResolver.classPathScript("schema.sql"))
                .start();
    }

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @Order(1)
    @DisplayName("부모가 없는 상위 카테고리를 추가할 수 있다.")
    void testInsert() {
        Category category = categoryRepository.insert(rootCategory);
        assertThat(category, samePropertyValuesAs(rootCategory));
    }

    @Test
    @Order(2)
    @DisplayName("부모가 있는 하위 카테고리를 추가할 수 있다.")
    void testSubInsert() {
        Category category = categoryRepository.insert(firstSubCategory);
        assertThat(category, samePropertyValuesAs(firstSubCategory));
    }

    @Test
    @Order(3)
    @DisplayName("카테고리를 id로 조회할 수 있다.")
    void testFindById(){
        Optional<Category> category = categoryRepository.findById(rootCategory.getCategoryId());
        assertThat(category.isEmpty(), is(false));
    }

    @Test
    @Order(4)
    @DisplayName("상위 카테고리로 하위 카테고리를 조회할 수 있다.")
    void testFindByCategory(){
        List<Category> categories = categoryRepository.findByParentId(rootCategory.getCategoryId());
        assertThat(categories.isEmpty(), is(false));
        assertThat(categories.size(), is(1));
        assertThat(categories.get(0), samePropertyValuesAs(firstSubCategory));

        List<Category> categories2 = categoryRepository.findByParentId(firstSubCategory.getCategoryId());
        assertThat(categories.isEmpty(), is(true));
    }

    @Test
    @Order(5)
    @DisplayName("카테고리를 수정할 수 있다.")
    void testUpdate(){
        Category anotherRootCategory = Category.builder()
                .categoryId(UUID.randomUUID())
                .categoryName("바지")
                .createdAt(Utils.now())
                .updatedAt(Utils.now())
                .build();
        Category wrongCategory = Category.builder()
                .categoryId(UUID.randomUUID())
                .categoryName("맨투맨")
                .parentId(anotherRootCategory.getCategoryId())
                .createdAt(Utils.now())
                .updatedAt(Utils.now())
                .build();

        categoryRepository.insert(anotherRootCategory);
        categoryRepository.insert(wrongCategory);

        wrongCategory.setCategoryName("후드");
        wrongCategory.setParentId(rootCategory.getCategoryId());

        categoryRepository.update(wrongCategory);

        Optional<Category> category = categoryRepository.findById(wrongCategory.getCategoryId());
        assertThat(category.isEmpty(), is(false));
        assertThat(category.get(), samePropertyValuesAs(wrongCategory));
    }

    @Test
    @Order(6)
    @DisplayName("카테고리를 삭제하면, 자식 카테고리도 삭제된다.")
    void testDeleteAll(){
        categoryRepository.deleteById(rootCategory.getCategoryId());
        Optional<Category> rootCategoryResult = categoryRepository.findById(rootCategory.getCategoryId());
        Optional<Category> subCategoryResult = categoryRepository.findById(firstSubCategory.getCategoryId());
        assertThat(rootCategoryResult.isEmpty(), is(true));
        assertThat(subCategoryResult.isEmpty(), is(true));
    }
}