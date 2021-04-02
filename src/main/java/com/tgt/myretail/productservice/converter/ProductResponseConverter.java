package com.tgt.myretail.productservice.converter;

import com.tgt.myretail.productservice.model.ProductPrice;
import com.tgt.myretail.productservice.response.PriceResponse;
import com.tgt.myretail.productservice.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductResponseConverter {
    public ProductResponse convert( ProductPrice productPrice, String productName){
        PriceResponse priceResponse = new PriceResponse(productPrice.getPrice(), productPrice.getCurrency_code());
        return new ProductResponse(productPrice.getProduct_id(), productName, priceResponse);
    }
}
