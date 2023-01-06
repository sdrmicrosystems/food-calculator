package com.fc.service;

import com.fc.exception.*;
import com.fc.model.Product;
import com.fc.model.ProductType;
import com.fc.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static com.fc.utils.ProductTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceTests {

    ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        List<Product> productList = Arrays.asList(
                createDummyProduct(1L, "name1", "desc1", BigDecimal.ZERO),
                createDummyProduct(2L, "name2", "desc2", BigDecimal.TEN),
                createDummyProduct(3L, "name3", "desc3", BigDecimal.ZERO)
        );
        productRepository.saveAll(productList);
        productService = new ProductService(productRepository);
    }

    @AfterEach
    public void destroyAll() {
        productRepository.deleteAll();
    }

    @Test
    void shouldReturnAllProductWithSuccess() {
        List<Product> productList = productRepository.findAll();
        assertThat(productList.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldCreateANewProductWithSuccess() {
        Product product = createDummyProduct("name", "desc", BigDecimal.ZERO);

        Product createdProduct = productService.createNewProduct(product);

        assertThat(createdProduct.getId() > 0);
        assertEquals(product.getName(), createdProduct.getName());
        assertEquals(product.getDescription(), createdProduct.getDescription());
        assertEquals(ProductType.HARD, createdProduct.getProductType());
        assertEquals(BigDecimal.ZERO, createdProduct.getPrice());
    }

    @Test
    void shouldReturnProductNameDoesNotProvidedExceptionWhenCreateNewProduct() {
        ProductNameDoesNotProvidedException exception = assertThrows(ProductNameDoesNotProvidedException.class, () -> {
            Product product = createDummyProduct(null, "desc", BigDecimal.ZERO);

            productService.createNewProduct(product);
        });

        assertEquals(ProductNameDoesNotProvidedException.class, exception.getClass());
    }

    @Test
    void shouldReturnProductNameAlreadyExistsExceptionWhenCreateNewProduct() {
        ProductNameAlreadyExistsException exception = assertThrows(ProductNameAlreadyExistsException.class, () -> {
            Product product = createDummyProduct("name1", "desc", BigDecimal.ZERO);

            productService.createNewProduct(product);
        });

        assertEquals(ProductNameAlreadyExistsException.class, exception.getClass());
    }

    @Test
    void shouldReturnAllProductsWithSuccess() {
        List<Product> productList = productService.getAllProducts();
        assertThat(productList.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldReturnOneProductsWithSuccess() {
        Product product = productService.getOneProduct(2l);

        assertEquals(2L, product.getId());
        assertEquals("name2", product.getName());
        assertEquals("desc2", product.getDescription());
        assertEquals(ProductType.HARD, product.getProductType());
        assertEquals(BigDecimal.TEN, product.getPrice());
    }

    @Test
    void shouldReturnProductNotFoundExceptionWhenCreateNewProduct() {
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getOneProduct(13L);
        });

        assertEquals(ProductNotFoundException.class, exception.getClass());
    }

    @Test
    void shouldReturnUpdatedProductWhenCallUpdateProduct(){
        assertEquals(3, productRepository.count());

        Product product = createDummyProduct(2l, "updatedName", "updatedDesc", BigDecimal.ONE);
        Product updatedProduct = productService.updateProduct(product, 2l);

        assertEquals("updatedName", updatedProduct.getName());
        assertEquals("updatedDesc", updatedProduct.getDescription());
        assertEquals(ProductType.HARD, updatedProduct.getProductType());
        assertEquals(ProductType.HARD.getName(), updatedProduct.getProductType().getName());
        assertEquals(BigDecimal.ONE, updatedProduct.getPrice());

        assertEquals(3, productRepository.count());
    }

    @Test
    void shouldReturnCreatedProductWhenCallUpdateProductAndItIdIsNull(){
        assertEquals(3, productRepository.count());

        Product product = createDummyProduct(null, "createdName", "createdDesc", BigDecimal.ONE);
        Product updatedProduct = productService.updateProduct(product, 20000l);

        assertThat(updatedProduct.getId() > 0);
        assertEquals("createdName", updatedProduct.getName());
        assertEquals("createdDesc", updatedProduct.getDescription());
        assertEquals(ProductType.HARD, updatedProduct.getProductType());
        assertEquals(BigDecimal.ONE, updatedProduct.getPrice());

        assertEquals(4, productRepository.count());
    }

    @Test
    void shouldDeleteTheProduct(){
        assertEquals(3, productRepository.count());

        productService.deleteProduct(Long.valueOf(2));

        assertEquals(2, productRepository.count());
    }

    @Test
    void shouldReturnMissingMandatoryFieldExceptionWhenChangePrice() {
        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class, () -> {
            productService.changePrice(2l, null);
        });

        assertEquals(MissingMandatoryFieldException.class, exception.getClass());
    }

    @Test
    void shouldReturnPriceIsNotGreaterThanZeroExceptionWhenChangePrice() {
        PriceIsNotGreaterThanZeroException exception = assertThrows(PriceIsNotGreaterThanZeroException.class, () -> {
            productService.changePrice(2l, BigDecimal.ZERO);
        });

        assertEquals(PriceIsNotGreaterThanZeroException.class, exception.getClass());
    }

    @Test
    void shouldReturnProductNotFoundExceptionWhenChangePrice() {
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.changePrice(2000000000l, new BigDecimal(123));
        });

        assertEquals(ProductNotFoundException.class, exception.getClass());
    }

    @Test
    void shouldReturnUpdatedProductWhenChangePrice() {
        Product product = productService.changePrice(2l, BigDecimal.TEN);

        assertEquals(BigDecimal.TEN, product.getPrice());
        assertEquals(ProductType.HARD, product.getProductType());
        assertEquals("name2", product.getName());
        assertEquals("desc2", product.getDescription());
    }

}
