package org.prgrms.awaker.global.enums;

public enum Boundary {
    MAX_PRODUCT_PRICE(10000000),
    MIN_PRODUCT_PRICE(100),
    MAX_PRODUCT_DESCRIPTION_LENGTH(500),
    MAX_PRODUCT_NAME_LENGTH(20);

    private final int value;

    Boundary(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
