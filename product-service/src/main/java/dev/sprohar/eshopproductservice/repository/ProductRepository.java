package dev.sprohar.eshopproductservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.sprohar.eshopproductservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
