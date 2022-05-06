package org.prgrms.awaker.global.enums;

public enum Constant {
    MAX_PRODUCT_PRICE(10000000),
    MIN_PRODUCT_PRICE(100),
    MAX_PRODUCT_DESCRIPTION_LENGTH(500),
    MAX_PRODUCT_NAME_LENGTH(20),
    MAX_CATEGORY_NAME_LENGTH(30),
    INIT_USER_POINT(50000),
    MAX_USER_NAME_LENGTH(20),
    MAX_USER_ADDRESS_LENGTH(200),
    MAX_USER_POSTCODE_LENGTH(200);

    private final int value;

    Constant(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
