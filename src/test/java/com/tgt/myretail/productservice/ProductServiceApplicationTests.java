package com.tgt.myretail.productservice;

import com.tgt.myretail.productservice.controller.ProductController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductServiceApplicationTests {

	@Autowired
	private ProductController controller;
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
