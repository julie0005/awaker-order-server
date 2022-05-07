package org.prgrms.awaker.domain.product.repository;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import org.junit.jupiter.api.*;
import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.domain.product.category.CategoryRepository;
import org.prgrms.awaker.global.Utils;
import org.prgrms.awaker.global.enums.Target;
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
class JdbcProductRepositoryTest {

    static EmbeddedMysql embeddedMysql;

    private final Category rootCategory = new Category(UUID.randomUUID(), "상의", Utils.now(), Utils.now());
    private final Category firstSubCategory = new Category(UUID.randomUUID(), "티셔츠", rootCategory.getCategoryId(), Utils.now(), Utils.now());
    private final Product newProduct = new Product(UUID.randomUUID(), firstSubCategory, "awaker 반팔티", 45000L, 38000L, Target.UNISEX, "", Utils.now(), Utils.now());

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

        categoryRepository.insert(rootCategory);
        categoryRepository.insert(firstSubCategory);
    }

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @Order(1)
    @DisplayName("상품을 추가할 수 있다.")
    void testInsert() {
        Product product = productRepository.insert(newProduct);
        assertThat(product, samePropertyValuesAs(newProduct));
        List<Product> all = productRepository.findAll();
        assertThat(all.isEmpty(), is(false));
    }

    @Test
    @Order(2)
    @DisplayName("상품을 아이디로 조회할 수 있다.")
    void testFindById(){
        Optional<Product> product = productRepository.findById(newProduct.getProductId());
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(3)
    @DisplayName("상품을 카테고리로 조회할 수 있다.")
    void testFindByCategory(){
        List<Product> product = productRepository.findByCategory(firstSubCategory);
        List<Product> productByParent = productRepository.findByCategory(rootCategory);
        assertThat(product.isEmpty(), is(false));
        assertThat(product.get(0).getCategory(), samePropertyValuesAs(firstSubCategory));
        assertThat(productByParent.get(0).getCategory(), samePropertyValuesAs(firstSubCategory));
        assertThat(product.get(0), samePropertyValuesAs(productByParent.get(0)));
    }

    @Test
    @Order(4)
    @DisplayName("상품을 수정할 수 있다.")
    void testUpdate(){
        newProduct.setProductName("awaker 22 s/s 반팔 티셔츠");
        productRepository.update(newProduct);

        Optional<Product> product = productRepository.findById(newProduct.getProductId());
        assertThat(product.isEmpty(), is(false));
        assertThat(product.get(), samePropertyValuesAs(newProduct));
    }

    @Test
    @Order(5)
    @DisplayName("상품을 전체 삭제한다.")
    void testDeleteAll(){
        productRepository.deleteAll();
        List<Product> all = productRepository.findAll();
        assertThat(all.isEmpty(), is(true));
    }
}