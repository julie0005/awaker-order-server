package org.prgrms.awaker.domain.order.repository;

public enum OrderSortMethod {
    RECENT("o.created_at desc"),
    PRICE("o.total_price desc");
    private final String query;

    OrderSortMethod(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
