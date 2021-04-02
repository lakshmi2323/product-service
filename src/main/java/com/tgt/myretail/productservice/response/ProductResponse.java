package com.tgt.myretail.productservice.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    Integer id;
    String name;
    PriceResponse current_price;
}
