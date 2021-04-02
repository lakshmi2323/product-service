package com.tgt.myretail.productservice.controller;
import com.tgt.myretail.productservice.response.ProductResponse;
import com.tgt.myretail.productservice.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "ProductDetailRestController", description = "REST Apis related to Product details")
@RestController
@RequestMapping("/products")
public class ProductController {

    public static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService; //Service which will do all data retrieval/manipulation work

    // Gets the product information that includes price from mongo data store and name from external API
    @ApiOperation(value = "Get specific product details - price and name", response = ProductResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "Not found!!")
    })

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductResponse getProduct(@PathVariable("id") Integer id) {
        logger.info("Fetching Product with id {}", id);
        ProductResponse getResponse = productService.findProductById(id);
        return getResponse;
    }

    // Updates existing product's price into mongo data store and returns response
    @ApiOperation(value = "Update specific product's price", response = ProductResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "Not found!!")
    })

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ProductResponse updateProduct(@PathVariable("id") Integer id,
                                         @RequestBody ProductResponse inputRequestBody) {
        logger.info("Updating existing Product's price with id {}", id);
        ProductResponse updateResponse = productService.updateProductPrice(id, inputRequestBody);
        return updateResponse;
    }
}
