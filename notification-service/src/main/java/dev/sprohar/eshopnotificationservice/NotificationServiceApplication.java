package dev.sprohar.eshopnotificationservice;

import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import dev.sprohar.eshopnotificationservice.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@SpringBootApplication
public class NotificationServiceApplication {

    private static final String TWILIO_ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    private static final String TWILIO_AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    private static final String FROM = System.getenv("TWILIO_PHONE");
    private static final String TO = "+14323493903";

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    private void sendNotification(OrderCreatedEvent orderCreatedEvent) {
        try {
            Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
            final String messageBody = String.format(
                    "Order %s has been placed.\n",
                    orderCreatedEvent.getOrderNumber().toString()
            );

            Message.creator(
                    new PhoneNumber(TO),
                    new PhoneNumber(FROM),
                    messageBody
            ).create();

            log.info("SMS was sent");
        } catch (TwilioException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * <a href="https://docs.spring.io/spring-kafka/docs/current/reference/html/#spring-boot-consumer-app">
     * Spring Boot Consumer App
     * </a>
     */
    @KafkaListener(id = "eShop", topics = "order_notifications")
    public void listen(OrderCreatedEvent orderCreatedEvent) {
        if (TWILIO_ACCOUNT_SID != null && TWILIO_AUTH_TOKEN != null && FROM != null) {
            sendNotification(orderCreatedEvent);
        } else {
            log.info("Order {} created.", orderCreatedEvent.getOrderNumber());
        }
    }
}
