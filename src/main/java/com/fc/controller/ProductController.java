package com.fc.controller;

import com.fc.exception.GlobalExceptionHandler;
import com.fc.model.Product;
import com.fc.model.ProductPriceChangeDTO;
import com.fc.service.ProductService;
import com.fc.util.AuthUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@OpenAPIDefinition(info = @Info(title = "Product API", version = "1.0.0", description = "CRUD product's functions"))
public class ProductController  extends GlobalExceptionHandler {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize(AuthUtil.ANY_ROLE)
    @Operation(description = "Get all products")
    List<Product> getAllProducts() {
        log.info("Receive request to receive all products");

        return productService.getAllProducts();
    }

    @PostMapping
    @PreAuthorize(AuthUtil.ADMIN_ROLE_ONLY)
    @Operation(description = "Create a new product")
    Product createNewProduct(@RequestBody Product newProduct) {
        log.info("Receive request to receive to save a new product");

        return productService.createNewProduct(newProduct);
    }

    @GetMapping("/{id}")
    @PreAuthorize(AuthUtil.ANY_ROLE)
    @Operation(description = "Get the product by id")
    ResponseEntity getOneProduct(@PathVariable Long id) {
        log.info("Receive request to find the product with id: {}", id);

        return ResponseEntity.ok(productService.getOneProduct(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize(AuthUtil.ADMIN_ROLE_ONLY)
    @Operation(description = "Update the product by id and new product's details")
    Product updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        log.info("Receive request to update the product with id: {}", id);

        return productService.updateProduct(newProduct, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(AuthUtil.ADMIN_ROLE_ONLY)
    @Operation(description = "Delete the product by id")
    void deleteProduct(@PathVariable Long id) {
        log.info("Receive request to delete the product with id: {}", id);

        productService.deleteProduct(id);
    }

    @PostMapping("/priceChange")
    @PreAuthorize(AuthUtil.ADMIN_ROLE_ONLY)
    @Operation(description = "Update product's price")
    Product changePrice(@RequestBody ProductPriceChangeDTO productPriceChangeDTO) {
        log.info("Receive request for price change. Product id: {}, new price: {}", productPriceChangeDTO.getId(), productPriceChangeDTO.getPrice());

        return productService.changePrice(productPriceChangeDTO.getId(), productPriceChangeDTO.getPrice());
    }

}
