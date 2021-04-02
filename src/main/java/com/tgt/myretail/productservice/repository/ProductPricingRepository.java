package com.tgt.myretail.productservice.repository;

import com.tgt.myretail.productservice.model.ProductPrice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPricingRepository extends MongoRepository<ProductPrice, String> {
    @Query(value = "{ 'product_id' : ?0 }")
    ProductPrice findProductPriceById(Integer product_id);
}
