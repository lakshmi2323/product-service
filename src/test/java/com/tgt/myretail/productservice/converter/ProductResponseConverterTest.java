package com.tgt.myretail.productservice.converter;

import com.tgt.myretail.productservice.model.ProductPrice;
import com.tgt.myretail.productservice.response.PriceResponse;
import com.tgt.myretail.productservice.response.ProductResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ProductResponseConverterTest {

    @InjectMocks
    ProductResponseConverter productResponseConverter;

    @Test
    public void TC001_convertProductNameAndPriceToProductResponseTest() {
        ProductPrice inputProductPrice = new ProductPrice();
        inputProductPrice.setProduct_id(123456789);
        inputProductPrice.setPrice(34.33F);
        inputProductPrice.setCurrency_code("USD");

        String inputProductName = "Test Product";

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setId(123456789);
        expectedResponse.setName(inputProductName);
        expectedResponse.setCurrent_price(new PriceResponse(34.33F, "USD"));

        ProductResponse outputResponse = productResponseConverter.convert(inputProductPrice, inputProductName);
        Assert.assertTrue(new ReflectionEquals(expectedResponse, null).matches(outputResponse));
    }
}
