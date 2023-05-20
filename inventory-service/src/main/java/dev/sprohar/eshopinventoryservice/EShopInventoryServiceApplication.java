package dev.sprohar.eshopinventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
	// @Bean
	// CommandLineRunner init(InventoryRepository inventoryRepository) {
	// return args -> {
	// Stream.of("123", "456", "789").forEach(sku -> {
	// Inventory inventory = new Inventory();
	// inventory.setSku(sku);
	// inventory.setQuantity(10);
	// inventoryRepository.save(inventory);
	// });
	// inventoryRepository.findAll().forEach(System.out::println);
	// };
	// }

}
