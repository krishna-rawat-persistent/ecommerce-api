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

-----
Here are some endpoints you can call:

##Product

### Create a product resource

```
++++ Admin Access only ++++

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

### Retrieve list of all the products

```
+++ Public Access +++

GET /api/product/

Response: HTTP 200
Content: application/json 
```

### Retrieve a product by id

```
+++ Public Access +++

GET /api/product/{productId}

Response: HTTP 200
Content: application/json 
```

### Update a Product

```
++++ Admin Access Only ++++

PUT /api/product/{productId}
Accept: multipart/form-data
Content-Type: application/json

{
 "name":"Embra Upholstered Wingback Chair",
 "category":"furniture",
 "brand": "Everly Quinn",
 "price":167.99,
 "color":["Gray","Blue","Dark Grey"],
 "size":null,
 "ratings":4.5,
 "description":"<ol><li>best in this category</li><li>light weight</li></ol>",
 "totalStock":67
}

RESPONSE: HTTP 200 (Ok)
```
### To view Swagger 3 API docs UI

Run the server and browse to localhost:8090/swagger-ui.html

### To view Swagger 3 API docs JSON

Run the server and browse to localhost:8090/v3/api-docs

# Running the project with PostgreSQL

This project uses PostgreSQL database where we are using db known as ```ecommercedb``` 

Here is what you would do to back the services with PostgreSQL, for example:

### In pom.xml add:

```
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
```

### Append this to the end of application.properties:

```
---

    #Database configurations
    spring.datasource.url=jdbc:postgresql://{Your Host}:5432/ecommercedb
    spring.datasource.username=<Your Postgres Username>
    spring.datasource.password=<Your Postgres Password>

---
```