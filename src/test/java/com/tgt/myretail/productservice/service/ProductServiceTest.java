package com.tgt.myretail.productservice.service;
import com.tgt.myretail.productservice.converter.ProductResponseConverter;
import com.tgt.myretail.productservice.model.ProductPrice;
import com.tgt.myretail.productservice.repository.ProductPricingRepository;
import com.tgt.myretail.productservice.response.PriceResponse;
import com.tgt.myretail.productservice.response.ProductResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductPricingRepository productPricingRepository;

    @Mock
    ProductResponseConverter productResponseConverter;

    @Before
    public void setUp(){
        ReflectionTestUtils.setField(productService, "rootURL", "https://redsky.target.com/v3/pdp/tcin/");
        ReflectionTestUtils.setField(productService, "excludeAttribute", "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics");
        ReflectionTestUtils.setField(productService, "keyAttribute", "key=candidate");
        ReflectionTestUtils.setField(productService, "productNameJsonPath","$['product']['item']['product_description']['title']");
    }

    @Test
    public void TC001_findProductByIdTest(){
        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct_id(12345678);
        productPrice.setPrice(13.99F);
        productPrice.setCurrency_code("USD");

        ProductResponse expectedProductResponse = new ProductResponse();
        expectedProductResponse.setId(productPrice.getProduct_id());
        expectedProductResponse.setName("Not Found");
        expectedProductResponse.setCurrent_price(new PriceResponse(productPrice.getPrice(), productPrice.getCurrency_code()));

        Mockito.doReturn(productPrice).when(productPricingRepository).findProductPriceById(productPrice.getProduct_id());
        Mockito.doReturn(expectedProductResponse).when(productResponseConverter).convert(productPrice, expectedProductResponse.getName());

        ProductResponse outputProductResponse = productService.findProductById(productPrice.getProduct_id());
        Assert.assertTrue(new ReflectionEquals(expectedProductResponse, null).matches(outputProductResponse));
    }

    @Test
    public void TC002_findProductByIdNotFoundTest(){
        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct_id(12345678);

        Mockito.doReturn(null).when(productPricingRepository).findProductPriceById(productPrice.getProduct_id());

        try{
            productService.findProductById(productPrice.getProduct_id());
            fail();
        }
        catch (ProductNotFoundException e){
            Assert.assertEquals("Product not found for the given product id", e.getMessage());
        }
    }

    @Test
    public void TC003_updateProductPriceTest(){
        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct_id(12345678);
        productPrice.setPrice(13.99F);
        productPrice.setCurrency_code("USD");

        ProductResponse expectedProductResponse = new ProductResponse();
        expectedProductResponse.setId(productPrice.getProduct_id());
        expectedProductResponse.setName("Baby Product");
        expectedProductResponse.setCurrent_price(new PriceResponse(productPrice.getPrice(), productPrice.getCurrency_code()));

        Mockito.doReturn(productPrice).when(productPricingRepository).findProductPriceById(any(Integer.class));
        Mockito.doReturn(productPrice).when(productPricingRepository).save(any(ProductPrice.class));
        Mockito.doReturn(expectedProductResponse).when(productResponseConverter).convert(any(ProductPrice.class), anyString());

        ProductResponse updateProductResponse = productService.updateProductPrice(productPrice.getProduct_id(), expectedProductResponse);
        Assert.assertTrue(new ReflectionEquals(expectedProductResponse, null).matches(updateProductResponse));
    }

    @Test
    public void TC004_updateProductPriceWithoutNameTest(){
        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct_id(12345678);
        productPrice.setPrice(13.99F);
        productPrice.setCurrency_code("USD");

        ProductResponse expectedProductResponse = new ProductResponse();
        expectedProductResponse.setId(productPrice.getProduct_id());
        expectedProductResponse.setName("TestProduct");
        expectedProductResponse.setCurrent_price(new PriceResponse(productPrice.getPrice(), productPrice.getCurrency_code()));

        ProductResponse inputRequest = new ProductResponse();
        inputRequest.setId(productPrice.getProduct_id());
        inputRequest.setCurrent_price(expectedProductResponse.getCurrent_price());

        Mockito.doReturn(productPrice).when(productPricingRepository).findProductPriceById(any(Integer.class));
        Mockito.doReturn(productPrice).when(productPricingRepository).save(any(ProductPrice.class));
        Mockito.doReturn(expectedProductResponse).when(productResponseConverter).convert(any(ProductPrice.class), anyString());

        ProductResponse updateProductResponse = productService.updateProductPrice(productPrice.getProduct_id(), inputRequest);
        Assert.assertTrue(new ReflectionEquals(expectedProductResponse, null).matches(updateProductResponse));
    }

    @Test
    public void TC005_updateProductPriceWithNullRequestTest() {
        try{
            productService.updateProductPrice(12345678, null);
            fail();
        }
        catch (ProductBadRequestException e){
            Assert.assertEquals("Invalid input request", e.getMessage());
        }
    }

    @Test
    public void TC006_updateProductPriceWithoutPriceObjectTest() {

        ProductResponse inputRequest = new ProductResponse();
        inputRequest.setId(12345678);
        inputRequest.setName("TestProduct");

        try{
            productService.updateProductPrice(inputRequest.getId(), inputRequest);
            fail();
        }
        catch (ProductBadRequestException e){
            Assert.assertEquals("Product price cannot be null", e.getMessage());
        }
    }

    @Test
    public void TC007_updateProductPriceWithoutPriceTest() {

        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setCurrency_code("USD");

        ProductResponse inputRequest = new ProductResponse();
        inputRequest.setId(12345678);
        inputRequest.setName("TestProduct");
        inputRequest.setCurrent_price(priceResponse);

        try{
            productService.updateProductPrice(inputRequest.getId(), inputRequest);
            fail();
        }
        catch (ProductBadRequestException e){
            Assert.assertEquals("Product price cannot be null", e.getMessage());
        }
    }

    @Test
    public void TC008_updateProductPriceNotFoundTest() {
        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct_id(12345678);
        productPrice.setPrice(13.99F);
        productPrice.setCurrency_code("USD");

        ProductResponse expectedProductResponse = new ProductResponse();
        expectedProductResponse.setId(productPrice.getProduct_id());
        expectedProductResponse.setName("Baby Product");
        expectedProductResponse.setCurrent_price(new PriceResponse(productPrice.getPrice(), productPrice.getCurrency_code()));

        Mockito.doReturn(null).when(productPricingRepository).findProductPriceById(any(Integer.class));

        try{
            productService.updateProductPrice(productPrice.getProduct_id(), expectedProductResponse);
            fail();
        }
        catch (ProductNotFoundException e){
            Assert.assertEquals("Product not found for the given product id", e.getMessage());
        }
    }

    @Test
    public void TC009_getProductNameFromExternalAPITest(){

        String outputProductName = productService.getProductName(13860428);
        String expectedProductNameFromExternalAPI = "The Big Lebowski (Blu-ray)";
        Assert.assertEquals(expectedProductNameFromExternalAPI, outputProductName);
    }
}
