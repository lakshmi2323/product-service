package com.tgt.myretail.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgt.myretail.productservice.response.PriceResponse;
import com.tgt.myretail.productservice.response.ProductResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ProductController productController;

    @Test
    public void TC001_getProductByIdTest() throws Exception {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(12345678);
        given(productController.getProduct(productResponse.getId())).willReturn(productResponse);
          mvc.perform(get("/products/"+ productResponse.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(productResponse.getId())));
    }

    @Test
    public void TC002_updateProductPriceByIdTest() throws Exception {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(12345678);
        productResponse.setCurrent_price(new PriceResponse(23.99F,"USD"));

        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(productResponse);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/products/"+ productResponse.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }
}
