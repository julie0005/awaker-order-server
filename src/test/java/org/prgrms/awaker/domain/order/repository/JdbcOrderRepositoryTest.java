package org.prgrms.awaker.domain.order.repository;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import org.junit.jupiter.api.*;
import org.prgrms.awaker.domain.order.Order;
import org.prgrms.awaker.domain.order.dto.OrderFilterDto;
import org.prgrms.awaker.domain.order.dto.OrderResDto;
import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.domain.user.User;
import org.prgrms.awaker.domain.user.UserRepository;
import org.prgrms.awaker.global.Utils;
import org.prgrms.awaker.global.enums.Authority;
import org.prgrms.awaker.global.enums.Gender;
import org.prgrms.awaker.global.enums.OrderStatus;
import org.prgrms.awaker.global.enums.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.prgrms.awaker.domain.order.*;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcOrderRepositoryTest {
    static EmbeddedMysql embeddedMysql;

    private Order newOrder;
    private final Category rootCategory = Category.builder()
            .categoryId(UUID.randomUUID())
            .categoryName("상의")
            .createdAt(Utils.now())
            .updatedAt(Utils.now())
            .build();
    private final Product newProduct = Product.builder()
            .productId(UUID.randomUUID())
            .category(rootCategory)
            .productName("awaker 반팔티")
            .price(45000L)
            .discountedPrice(38000L)
            .targetUser(Target.UNISEX)
            .description("")
            .createdAt(Utils.now())
            .updatedAt(Utils.now())
            .build();
    private final User user = User.builder()
            .userId(UUID.randomUUID())
            .userName("김승은")
            .age(23)
            .auth(Authority.ADMIN)
            .email("julie0005@ajou.ac.kr")
            .gender(Gender.FEMALE)
            .password("test1234!")
            .build();
    private final OrderItem orderItem = OrderItem.builder()
            .productId(newProduct.getProductId())
            .productTotalDiscount(3000L)
            .quantity(3)
            .productTotalPrice(newProduct.getDiscountedPrice()*3-3000L)
            .build();
    private List<OrderItem> orderItems = new ArrayList<>();
    private Order inserted;

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

        userRepository.insert(user);
    }

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void domainSetUp(){
        orderItems.add(orderItem);
        newOrder = Order.builder()
                .userId(user.getUserId())
                .orderId(UUID.randomUUID())
                .address("경기도 용인시")
                .postcode("213432")
                .totalPrice(50000)
                .totalDiscount(10000L)
                .orderItems(orderItems)
                .build();
        inserted = orderRepository.insert(newOrder);
    }

    @AfterEach
    void domainEndUp(){
        orderItems.clear();
        newOrder = null;
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("주문을 추가할 수 있다.")
    void testInsert() {
        assertThat(inserted, samePropertyValuesAs(newOrder));
        List<OrderResDto> all = orderRepository.findAll(OrderSortMethod.RECENT);
        assertThat(all.isEmpty(), is(false));
    }

    @Test
    @DisplayName("주문을 아이디로 조회할 수 있다.")
    void testFindById(){
        Optional<OrderResDto> orderDto = orderRepository.findById(newOrder.getOrderId());
        assertThat(orderDto.isEmpty(), is(false));
    }

    @Test
    @DisplayName("주문을 전체 조회할 수 있다.")
    void testFindAll(){
        List<OrderResDto> all = orderRepository.findAll(OrderSortMethod.RECENT);
        assertThat(all.isEmpty(), is(false));
    }

    @Test
    @DisplayName("주문 가격이 비싼 순으로 전체 조회할 수 있다.")
    void testFindAllWithPriceSort(){
        Order secondOrder = Order.builder()
                .userId(user.getUserId())
                .orderId(UUID.randomUUID())
                .address("경기도 용인시")
                .postcode("213432")
                .totalPrice(60000)
                .totalDiscount(10000L)
                .orderItems(orderItems)
                .build();
        orderRepository.insert(secondOrder);
        List<OrderResDto> all = orderRepository.findAll(OrderSortMethod.PRICE);
        assertThat(all.size(), is(2));
        assertThat(all.get(0).totalPrice(), greaterThanOrEqualTo(all.get(1).totalPrice()));
    }

    @Test
    @DisplayName("주문을 필터링하여 조회할 수 있다.")
    void testFilter(){
        Order secondOrder = Order.builder()
                .userId(user.getUserId())
                .orderId(UUID.randomUUID())
                .address("경기도 용인시")
                .postcode("213432")
                .totalPrice(60000)
                .totalDiscount(10000L)
                .orderItems(orderItems)
                .build();
        orderRepository.insert(secondOrder);
        OrderFilterDto filterDto = OrderFilterDto.builder()
                .priceGreaterThan(secondOrder.getTotalPrice())
                .build();
        List<OrderResDto> all = orderRepository.findByFilter(filterDto, OrderSortMethod.RECENT);
        assertThat(all.size(), is(1));
        assertThat(all.get(0).orderId(), equalTo(secondOrder.getOrderId()));
    }

    @Test
    @DisplayName("주문을 수정할 수 있다.")
    void testUpdate(){
        newOrder.setOrderStatus(OrderStatus.READY_FOR_DELIVERY);
        newOrder.setAddress("친구네 집");
        orderRepository.update(newOrder);
        Optional<OrderResDto> orderDto = orderRepository.findById(newOrder.getOrderId());
        assertThat(orderDto.isEmpty(), is(false));
        assertThat(orderDto.get().orderId(), equalTo(newOrder.getOrderId()));
        assertThat(orderDto.get().orderStatus(), equalTo(newOrder.getOrderStatus()));
        assertThat(orderDto.get().address(), equalTo(newOrder.getAddress()));
    }

    @Test
    @DisplayName("Order와 OrderItem price 유효성 검사 - TODO")
    void testPriceValidation(){
        long expectedTotalPrice = orderItems.stream().mapToLong(OrderItem::getProductTotalPrice).sum();
        assertThat(newOrder.getTotalPrice(), not(expectedTotalPrice));
    }
}