package dev.sprohar.eshoporderservice.error;

public class ItemsUnavailableException extends RuntimeException {

    public ItemsUnavailableException(String message) {
        super(message);
    }
}
