package dev.sprohar.eshopproductservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.sprohar.eshopproductservice.dto.CreateProductDto;
import dev.sprohar.eshopproductservice.model.Product;
import dev.sprohar.eshopproductservice.repository.ProductRepository;

/**
 * Reference:
 * 
 * <a href="https://spring.io/guides/gs/testing-web/">
 * Testing Spring Web Applications
 * </a>
 */
@SpringBootTest
@AutoConfigureMockMvc // for testing controllers
@Testcontainers
class EShopProductServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ProductRepository productsRepository;

	@Container
	static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.2.24"));

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@AfterEach
	void cleanUp() {
		this.productsRepository.deleteAll();
	}

	private CreateProductDto getCreateProductDto() {
		return CreateProductDto.builder().name("Product 1").price(BigDecimal.valueOf(10.0)).build();
	}

	@Test
	void shouldCreateProductWhenValidDataIsGiven() throws Exception {
		CreateProductDto dto = this.getCreateProductDto();
		String body = this.mapper.writeValueAsString(dto);

		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/api/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		assertEquals(1, this.productsRepository.count());
	}

	@Test
	void shouldDeleteProductWhenProductExists() throws Exception {
		Product product = this.productsRepository.insert(
				Product.builder()
						.name("Product 1")
						.price(BigDecimal.valueOf(10.0))
						.build());

		assertEquals(1, this.productsRepository.count());

		this.mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/products/" + product.getId()))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

		assertEquals(0, this.productsRepository.count());
	}

	@Test
	void shouldNotDeleteProductWhenProductDoesNotExist() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/products/1"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void shouldReturnProductList() throws Exception {
		Product product = this.productsRepository.insert(
				Product.builder()
						.name("Product 1")
						.price(BigDecimal.valueOf(10.0))
						.build());

		assertEquals(1, this.productsRepository.count());

		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/api/products"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(product.getId()));
	}

	@Test
	void shouldReturnProductByIdWhenProductExists() throws Exception {
		Product product = this.productsRepository.insert(
				Product.builder()
						.name("Product 1")
						.price(BigDecimal.valueOf(10.0))
						.build());

		assertEquals(1, this.productsRepository.count());

		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/api/products/" + product.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(product.getId()));
	}

	@Test
	void shouldNotReturnProductByIdWhenProductDoesNotExist() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/api/products/1"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void shouldUpdateProduct() throws Exception {
		Product product = this.productsRepository.insert(
				Product.builder()
						.name("Product 1")
						.price(BigDecimal.valueOf(10.0))
						.build());

		assertEquals(1, this.productsRepository.count());

		CreateProductDto dto = this.getCreateProductDto();
		dto.setName("Product 2");

		String body = this.mapper.writeValueAsString(dto);

		this.mockMvc.perform(
				MockMvcRequestBuilders.patch("/api/products/" + product.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

		Optional<Product> optional = this.productsRepository.findById(product.getId());
		if (optional.isEmpty()) {
			fail("Could not find product " + product.getId());
		}

		assertEquals("Product 2", optional.get().getName());
	}

	@Test
	void shouldNotUpdateProductWhenProductDoesNotExist() throws Exception {
		CreateProductDto dto = this.getCreateProductDto();
		String body = this.mapper.writeValueAsString(dto);

		this.mockMvc.perform(
				MockMvcRequestBuilders.patch("/api/products/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

}
