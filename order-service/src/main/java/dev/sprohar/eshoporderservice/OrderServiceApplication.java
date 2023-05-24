package dev.sprohar.eshoporderservice;

import dev.sprohar.eshoporderservice.events.OrderCreatedEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceApplication {

    public static final String ORDER_NOTIFICATIONS_TOPIC = "order_notifications";

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, OrderCreatedEvent> template) {
        return args -> {
            var future = template.send(
                    ORDER_NOTIFICATIONS_TOPIC,
                    OrderCreatedEvent.builder()
                            .orderNumber(1L)
                            .customerId("1")
                            .customerCellPhone("+1555555555")
                            .build()
            );

            future.whenComplete((result, err) -> {
               if (err != null) {
                   System.err.println(err.getMessage());
               } else {
                   System.out.println(result.toString());
               }
            });
        };
    }
}
