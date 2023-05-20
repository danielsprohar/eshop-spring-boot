package dev.sprohar.eshopinventoryservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.sprohar.eshopinventoryservice.model.Inventory;
import dev.sprohar.eshopinventoryservice.repository.InventoryRepository;

@SpringBootApplication
public class EShopInventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EShopInventoryServiceApplication.class, args);
	}

	/**
	 * Initialize the database with some test entries
	 * 
	 * @param inventoryRepository
	 * @return
	 */
	@Bean
	CommandLineRunner init(InventoryRepository inventoryRepository) {
		List<Inventory> inventory = new ArrayList<>();
		inventory.add(Inventory.builder().sku(1L).quantity(10).build());
		inventory.add(Inventory.builder().sku(2L).quantity(10).build());
		inventory.add(Inventory.builder().sku(3L).quantity(0).build());

		return args -> {
			inventory.stream()
					.forEach(item -> {
						inventoryRepository.save(item);
					});
		};
	}

}
