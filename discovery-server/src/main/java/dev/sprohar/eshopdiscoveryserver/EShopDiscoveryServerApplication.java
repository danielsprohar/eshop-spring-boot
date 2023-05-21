package dev.sprohar.eshopdiscoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EShopDiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EShopDiscoveryServerApplication.class, args);
	}

}
