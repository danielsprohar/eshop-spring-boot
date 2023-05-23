package dev.sprohar.eshoporderservice.enums;

public enum EShopTopics {

    ORDER_NOTIFICATIONS("order_notifications");

    private final String topic;

    EShopTopics(String status) {
        this.topic = status;
    }

    @Override
    public String toString() {
        return this.topic;
    }
}
