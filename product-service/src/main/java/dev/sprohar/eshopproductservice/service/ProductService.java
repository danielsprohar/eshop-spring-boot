package dev.sprohar.eshopproductservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.sprohar.eshopproductservice.error.ProductNotFoundException;
import dev.sprohar.eshopproductservice.model.Product;
import dev.sprohar.eshopproductservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void create(Product product) {
        this.productRepository.insert(product);
        log.info("Product {} was created", product.getSku());
    }

    public void delete(String id) {
        this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        this.productRepository.deleteById(id);
        log.info("Product {} was deleted", id);
    }

    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    public Product getById(String id) {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public void update(Product product) {
        this.productRepository.save(product);
        log.info("Product {} was updated", product.getSku());
    }
}
