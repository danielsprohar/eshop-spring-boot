package dev.sprohar.eshopproductservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EShopProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EShopProductServiceApplication.class, args);
	}

}
