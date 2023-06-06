# e-Commerce API 

This is the REST API for e-Commerce, and it is based on Java / Maven / Spring Boot (version 2.7.12) with PostgreSQL as database support. 

## How to Run

This application is packaged as a jar which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary. You run it using the ```java -jar``` command.

* Clone this repository
* Make sure you are using JDK 1.11 and Maven 3.x
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by one of these two methods:
```
        java -jar target/ecommerce_api-0.0.1.jar
or
        mvn spring-boot:run
```

Once the application runs you should see something like this

```
2023-06-05 13:55:54.851  INFO 15504 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8090 (http) with context path ''
2023-06-05 13:55:55.291  INFO 15504 --- [           main] c.w.ecommerce.EcommerceApiApplication    : Started EcommerceApiApplication in 8.488 seconds (JVM running for 8.993)
```

## About the Service

The service is an e-Commerce REST service. It uses PostgreSQL database to store the data. you can call some REST endpoints defined in ```com.workflow2.ecommerce.ProductController``` on **port 8090**. (see below)

You can use this REST app to perform end-to-end ecommerce operations like login-register, get Products, add to cart etc.

Here is what this little application demonstrates:

* Full integration with the latest **Spring** Framework: inversion of control, dependency injection, etc.
* Packaging as a single jar with embedded container (tomcat 8): No need to install a container separately on the host just run using the ``java -jar`` command
* Writing a RESTful service using annotation: supports both XML and JSON request / response; simply use desired ``Accept`` header in your request
* Exception mapping from application exceptions to the right HTTP response with exception details in the body
* *Spring Data* Integration with JPA/Hibernate with just a few lines of configuration and familiar annotations.
* All APIs are "self-documented" by Swagger3 using annotations

Here are some endpoints you can call:

### Create a product resource

```
POST /api/product/
Accept: application/Multipart_Form_Data
Content-Type: application/json

{
 "name":"Stalder Transparent Lantern Head",
 "category":"lighting",
 "brand": " Red Barrel Studio®",
 "price":52.99,
 "color":["#4EBF9C","#17B383","#5B726B"],
 "size":null,
 "description":"<p>The post lights is made up of sturdy metal construction and glass shade which makes it perfect for wet location such as garden, backyard, courtyard, patio, balcony, porch, pathway or entryway.</p><ol><li>12 H X 6.1 W X 6.1 D</li></ol>",
"totalStock":54,
 "ratings":4.8	
}

RESPONSE: HTTP 201 (Created)
Location header: http://localhost:8090/api/product/
```

### Retrieve a paginated list of products

```
http://localhost:8090/api/product/?page=0&size=10

Response: HTTP 200
Content: paginated list 
```

### Update a Product

```
PUT /api/product/{productId}
