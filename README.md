# food-calculator (WORK IN PROGRESS)
Food Calculator a management application of consumer products based on nutritional value

https://www.calculator.net/calorie-calculator.html
https://tools.myfooddata.com/nutrition-facts-database-spreadsheet.php
https://caloriecontrol.org/healthy-weight-tool-kit/food-calorie-calculator/
https://www.calorieking.com/us/en/foods/
https://www.calories.info/calorie-intake-calculator
https://www.calories.info/food/fruits

## Summary
1. Functionalities
2. Getting Started (Prerequisites, Installing)
    - 2.1 Prerequisites
    - 2.2 Database installation
        - 2.2.1 H2
        - 2.2.2 MySQL
        - 2.2.3 PostgreSQL
3. Running the tests
4. Deployment
   4.1 Deployment & run on LINUX environment
   4.2 Deployment & run on WINDOWS environment
5. Swagger usage
6. Built With
7. Contributing
8. Versioning
9. License

## 1. Functionalities

### 1.1 Get all products
Return the products as a list. 
Use the following curl `curl -v localhost:8081/products` or Swagger-UI (see point 5) to access the end-point.

### 1.2 Create a new product
Use this functionality to create the products.
Use the below curl or Swagger-UI (see point 5) to access the end-point.
`curl -X POST -H "Content-type:application/json" --data-binary "{\"name\": \"test\", \"description\": \"test\", \"price\": 10}" http://localhost:8081/products`

### 1.3 Get the product by id
Return the product by ID value.
Use the following curl `curl -v localhost:8081/products/99` or Swagger-UI (see point 5) to access the end-point.

### 1.4 Update the product by id and new product's details
Use this functionality to create the products.
Use the below curl or Swagger-UI (see point 5) to access the end-point.
`curl -X PUT -H "Content-type:application/json" --data-binary "{\"name\": \"test updated\", \"description\": \"test updated\", \"price\": 10}" http://localhost:8081/products/37`

### 1.5 Delete the product by id
Delete the product by ID value.
Use the following curl `curl -X DELETE localhost:8081/products/1` or Swagger-UI (see point 5) to access the end-point.

## 2. Getting Started

Clone or download a copy of this project.

### 2.1 Prerequisites

This project requires Java 11, Maven and at least one database (PostgreS, H2, MySql).
Please, see `SecurityConfig` class for more information.
WARN: If no security config then change the value from `spring.security.user.password` property. User `user` value as default username.

### 2.2 Database installation

#### 2.2.1 H2
No installation is required.
The `spring.datasource.url` is the one required property which should be set. By default, the
username is `sa` with empty password. Two modes: in memory and file storage. See the `application.properties`
file for more details related configuration.

#### 2.2.2 MySQL

```
CREATE DATABSE stm;
```

Note: in case that you run the application starting with MySQL 8.0.4, please execute the following query:
```
ALTER USER '${USER}'@'localhost' IDENTIFIED WITH mysql_native_password BY '${PASSWORD}';
-- where ${USER} and ${PASSWORD} should be provided. 
```

#### 2.2.3 Postgres
Install PostgreSQL. It is required to create a database:

Please, run the following commands if it is the case:
```
createuser -U postgres -s Progress
```

Please, run the following command to import a database (if it is the case):
```
pg_restore -d DATABASE_NAME <  PATH/BACKUP_FILE_NAME.sql
```

To create the JAR file please use the following command:
```
mvn clean package
```

## 3. Running the tests
Find all project test into `src/test/java` path. Per overall description:
- `com.fc.controller` contains all integration tests
- `com.fc.service` contains all unit tests
- `com.fc.utils` util class for testing propose
## 4. Deployment
If the build (the jar file) is ready then the application can be run. Please, use the following command to run the application:
```
XXX:food-calculator xxx java -jar target/food-calculator-X.Y.Z-SNAPSHOT.jar
```

### 4.1 Deployment & run on LINUX environment
In case if application is run in a linux based instance, please create the following folders:
1. /app         - folder where the JAR is located;
2. /app/log     - folder which will contains the logs;
3. /app/config  - folder which will contains the application configuration files;
4. /app/file-db - production or working folder where service will save physically the content;
5. /app/test-db - test folder where service will save physically the content;

To run the application we have the following options:
```
nohub java -jar food-calculator-1.0.0.jar --spring.config.location=/app/config/application.properties &
```

To show last NUM_OF_RECORDS from a FILE_NAME linux command:
```
tail -n NUM_OF_RECORDS FILE_NAME
```

### 4.2 Deployment & run on WINDOWS environment
The application was developed on Windows environment.

To run application, please run this command:
```
java -jar /projects/app/food-calculator-1.0.0.jar --spring.config.location=/projects/app/config/application.properties
```

## 5. Swagger usage
This will automatically deploy swagger-ui to a spring-boot application:
Documentation will be available in HTML format, using the official swagger-ui jars
The Swagger UI page will then be available at `http://server:port/context-path/swagger-ui.html` and the OpenAPI description will be available at the following url for json format:`http://server:port/context-path/v3/api-docs`
- `server` The server name or IP
- `port` The server port
- `context-path` The context path of the application (if exist)

To access the Swagger UI, please use the following links:
-  ``http://localhost:8081/swagger-ui/index.html``
-  ``http://localhost:8081/swagger-ui.html``

## 6. Built With

* [Java](https://www.java.com/en/download/) - Java technology allows you to work and play in a secure computing environment. Java allows you to play online games, chat with people around the world, calculate your mortgage interest, and view images in 3D, just to name a few.
* [Spring Security](https://spring.io/projects/spring-security) - Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications.
* [Spring Boot](https://spring.io/projects/spring-boot) - Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
* [Spring Data](https://spring.io/projects/spring-data) - Spring Data’s mission is to provide a familiar and consistent, Spring-based programming model for data access while still retaining the special traits of the underlying data store.
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - Spring Data JPA, part of the larger Spring Data family, makes it easy to easily implement JPA based repositories. This module deals with enhanced support for JPA based data access layers. It makes it easier to build Spring-powered applications that use data access technologies.
* [Spring Doc](https://springdoc.org/) - Java library helps to automate the generation of API documentation using spring boot projects..
* [H2](https://www.h2database.com/) - H2 is a relational database management system written in Java. It can be embedded in Java applications or run in client-server mode.
* [PostgreSQL](https://www.postgresql.org/) - PostgreSQL, also known as Postgres, is a free and open-source relational database management system (RDBMS) emphasizing extensibility and technical standards compliance. It is designed to handle a range of workloads, from single machines to data warehouses or Web services with many concurrent users. It is the default database for macOS Server, and is also available for Linux, FreeBSD, OpenBSD, and Windows.
* [Maven](https://maven.apache.org/) - Apache Maven is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information.

## 7. Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## 8. Versioning

We use [SemVer](http://semver.org/) for versioning.

## 9. License
`Apache License 2.0` in general.
If you want to run the system in production and have questions or need advices then please drop a [message](sdr.microsystems.services@gmail.com).
