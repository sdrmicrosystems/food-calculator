package com.fc;

import com.fc.controller.ProductController;
import com.fc.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StoreManagementToolApplicationTests {

	@Autowired
	ProductController productController;

	@Autowired
	ProductService productService;

	@Test
	void contextLoads() {
		assertThat(productController).isNotNull();
		assertThat(productService).isNotNull();
	}

}
