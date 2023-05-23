package dev.sprohar.eshoporderservice;

import dev.sprohar.eshoporderservice.enums.EShopTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
@EnableDiscoveryClient
public class EShopOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EShopOrderServiceApplication.class, args);
	}

	/**
	 * Reference: <br />
	 * <a href="https://docs.spring.io/spring-kafka/docs/current/reference/html/#spring-boot-producer-app">
	 * Spring Boot Producer App
	 * </a>
	 * @return A new topic
	 */
	@Bean
	public NewTopic topic() {
		return TopicBuilder.name(EShopTopics.ORDER_NOTIFICATIONS.toString())
				.partitions(10)
				.replicas(1)
				.build();
	}
}
