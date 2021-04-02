package com.tgt.myretail.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Mongo database annotation.
@Document(collection= "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPrice {
    @Id
    private String id;
    private Integer product_id;
    private Float price;
    private String currency_code;
}
