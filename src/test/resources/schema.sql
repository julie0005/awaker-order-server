CREATE TABLE products
(
    product_id BINARY(16) PRIMARY KEY,
    product_name VARCHAR(20) NOT NULL,
    target VARCHAR(10) NOT NULL,
    category_id BINARY(16) NOT NULL,
    price bigint NOT NULL,
    discounted_price bigint DEFAULT NULL,
    description VARCHAR(500) DEFAULT NULL,
    created_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at datetime(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX (category_id)
);

CREATE TABLE category
(
    category_id BINARY(16) PRIMARY KEY,
    category_name VARCHAR(30) NOT NULL,
    parent_id BINARY(16) DEFAULT NULL,
    created_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at datetime(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_category_parent FOREIGN KEY(parent_id) REFERENCES  category(category_id) ON DELETE CASCADE
);

CREATE TABLE users
(
    user_id BINARY(16) PRIMARY KEY,
    user_name VARCHAR(20) NOT NULL,
    gender VARCHAR(5) NOT NULL,
    age int NOT NULL,
    email VARCHAR(50) NOT NULL,
    address VARCHAR(200) DEFAULT NULL,
    postcode VARCHAR(200) DEFAULT NULL,
    password VARCHAR(100) NOT NULL,
    authority VARCHAR(10) NOT NULL,
    description VARCHAR(500) DEFAULT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'NONE',
    point int NOT NULL DEFAULT 5000,
    created_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at datetime(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

CREATE TABLE orders
(
    order_id BINARY(16) PRIMARY KEY,
    user_id binary(16)  NOT NULL,
    total_price bigint NOT NULL,
    total_discount bigint NOT NULL DEFAULT 0,
    address VARCHAR(200) NOT NULL,
    postcode VARCHAR(200) NOT NULL,
    order_status  VARCHAR(20) NOT NULL DEFAULT 'ACCEPTED',
    created_at    datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    datetime(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

CREATE TABLE order_items
(
    item_id bigint  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id    binary(16)  NOT NULL,
    product_id  binary(16)  NOT NULL,
    product_total_price   bigint  NOT NULL,
    product_total_discount bigint NOT NULL DEFAULT 0,
    quantity    int NOT NULL DEFAULT 1,
    created_at    datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    datetime(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX (order_id)
);

CREATE TABLE carts
(
    cart_id BINARY(16) PRIMARY KEY,
    user_id binary(16)  NOT NULL,
    product_id  binary(16)  NOT NULL,
    created_at    datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    INDEX (user_id),
    INDEX (product_id)
);