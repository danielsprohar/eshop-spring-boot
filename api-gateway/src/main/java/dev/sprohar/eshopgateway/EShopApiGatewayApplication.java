package dev.sprohar.eshopgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EShopApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EShopApiGatewayApplication.class, args);
	}

}
