package com.fc.service;

import com.fc.exception.*;
import com.fc.model.Product;
import com.fc.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product createNewProduct(Product newProduct) {
        final String name = newProduct.getName();
        if(name == null || name.isEmpty()){
            log.warn("The product name was not provided.");
            throw new ProductNameDoesNotProvidedException();
        }

        if(productRepository.findByName(name) != null) {
            log.warn("The product name already exists. Name: {}", name);
            throw new ProductNameAlreadyExistsException(name);
        }

        return productRepository.save(newProduct);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getOneProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return product;
    }

    public Product updateProduct(Product newProduct, Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setDescription(newProduct.getDescription());
                    product.setPrice(newProduct.getPrice());
                    product.setUpdateDate(Instant.now());
                    return productRepository.save(product);
                })
                .orElseGet(() -> {
                    newProduct.setCreateDate(Instant.now());
                    return productRepository.save(newProduct);
                });
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product changePrice(Long id, BigDecimal price) {
        if(price == null) {
            throw new MissingMandatoryFieldException("The field price is mandatory!");
        }

        if(price.compareTo(BigDecimal.ZERO) == 0){
            throw new PriceIsNotGreaterThanZeroException(id);
        }

        return productRepository.findById(id).map(product -> {
            product.setPrice(price);
            return productRepository.save(product);
        }).orElseGet(() -> {
            throw new ProductNotFoundException(id);
        });
    }

}
