package dev.sprohar.eshopinventoryservice.error;

public class ItemNotFoundException extends RuntimeException {
    public  ItemNotFoundException(String message) {
        super(message);
    }
}
