# product-service
RESTful service to read product price from mongo data store, name from external API and update product price in mongo data store.

Steps to run service:
---------------------
1. Install mongo and run both server and client instances in the system.
   [In windows command prompt, to start server, use 'mongod' and 'mongo' for client]
   Insert script to add product price in mongo collection named 'products' of database 'Test'
   
   Below are commands to create database, adding product price objects into collection 'products'
   
   [creating database 'Test']   
   use Test
   
   [adding products to collection named 'products']
   
   db.products.insertMany([
   {product_id: 13860428, price: 13.49, currency_code: "USD"},
   {product_id: 54456119, price: 4.99, currency_code: "USD"},
   {product_id: 13264003, price: 3.99, currency_code: "USD"},
   {product_id: 12954218, price: 4.88, currency_code: "USD"}])
   
   [to view the added products]   
   db.products.find().pretty()
   
2. download the gradle project from below git link and run it with any IDE (Intellij ultimate, eclipse)
    https://github.com/lakshmi2323/product-service
    
3. In the postman locally, GET and PUT end points can be executed with below port
   
   GET : http://localhost:8102/products/13860428?  
   PUT : http://localhost:8102/products/13860428?
   
        Requestbody:
        {            
            "current_price": {
                "value": 43.99
            }
        }
        
 Note: End points are running fine locally and unit tests are verified.
 
  
   
