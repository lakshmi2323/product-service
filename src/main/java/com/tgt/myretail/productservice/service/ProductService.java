package com.tgt.myretail.productservice.service;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.tgt.myretail.productservice.converter.ProductResponseConverter;
import com.tgt.myretail.productservice.model.ProductPrice;
import com.tgt.myretail.productservice.repository.ProductPricingRepository;
import com.tgt.myretail.productservice.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductService {

    @Autowired
    ProductPricingRepository productPricingRepository;

    @Autowired
    ProductResponseConverter productResponseConverter;

    @Value("${externalapi.rooturl}")
    String rootURL;

    @Value("${externalapi.excludeattribute}")
    String excludeAttribute;

    @Value("${externalapi.keyattribute}")
    String keyAttribute;

    ProductPrice productPrice;

    public ProductResponse findProductById (Integer productId){
        // get id and find price store from mongodb
        productPrice = getProductPrice(productId);

        // call external API and get product name
        String productName = getProductName(productId);
        // combine these info as response
        return productResponseConverter.convert(productPrice, productName);
    }

    public ProductResponse updateProductPrice (Integer productId, ProductResponse inputRequest) {
        if(inputRequest == null){
            throw new ProductBadRequestException("Invalid input request");
        }

        if(inputRequest.getCurrent_price() == null || inputRequest.getCurrent_price().getValue() == null){
            throw new ProductBadRequestException("Product price cannot be null");
        }

        // get price store from mongodb with the given product id
        productPrice = getProductPrice(productId);

        ProductPrice priceToUpdate = new ProductPrice();
        priceToUpdate.setId(productPrice.getId());
        priceToUpdate.setProduct_id(productPrice.getProduct_id());
        priceToUpdate.setCurrency_code(productPrice.getCurrency_code());
        priceToUpdate.setPrice(inputRequest.getCurrent_price().getValue());

        // update price in existing product price store
        productPricingRepository.save(priceToUpdate);

        String productName;
        if(inputRequest.getName() == null){
            productName = getProductName(productId);
        }
        else {
            productName = inputRequest.getName();
        }

        // combine updated product price and name as response
        return productResponseConverter.convert(getProductPrice(productId), productName);
    }

    public String getProductName(Integer productId) {
        final String uri = rootURL + productId + "?" + excludeAttribute +"&" +keyAttribute;
        RestTemplate restTemplate = new RestTemplate();
        String productName;
        try{
            ResponseEntity<String> responseJSONString = restTemplate.getForEntity(uri, String.class);
            productName = parseProductName(responseJSONString.getBody());
        }
        catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                productName = "Not Found";
            }
            else
                throw new ProductNotFoundException("Error in calling external API - " + e.getStatusCode());
        }

        return productName;
    }

    public String parseProductName(String jsonDataSourceString)  {
        String jsonProductNamePath = "$['product']['item']['product_description']['title']";
        DocumentContext jsonContext = JsonPath.parse(jsonDataSourceString);
        return jsonContext.read(jsonProductNamePath);
    }

    public ProductPrice getProductPrice(Integer productId){
        productPrice = productPricingRepository.findProductPriceById(productId);
        if(productPrice == null){
            throw new ProductNotFoundException("Product not found for the given product id");
        }
        return productPrice;
    }
}
