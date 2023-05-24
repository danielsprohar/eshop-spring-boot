package dev.sprohar.eshopinventoryservice;

import dev.sprohar.eshopinventoryservice.model.Inventory;
import dev.sprohar.eshopinventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(InventoryRepository inventoryRepository) {
		List<Inventory> inventory = new ArrayList<>();
		inventory.add(Inventory.builder().sku(1L).quantity(10).build());
		inventory.add(Inventory.builder().sku(2L).quantity(10).build());
		inventory.add(Inventory.builder().sku(3L).quantity(0).build());

		return args -> {
			inventoryRepository.saveAll(inventory);
		};
	}

}
