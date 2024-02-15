# Spring Framework main Practices
---

## Below you will find the short description of each practice in thisrepository. Also you can get exact information about each of them in the README.md file for each of them, just click on the link to see more details.
---
👏
---

### [First practice](https://github.com/dmitriy-utkin/spring-framework-practices/tree/main/spring-first-practice-contacts)
![Static Badge](https://img.shields.io/badge/Spring-boot-green)

The Spring Course Practices project is a console application for managing contacts. It uses profiles for applications and bean injections. 
The main features include printing all contacts, adding new contacts, removing contacts, and stopping the application. 
The model structure consists of a Contact class with fields for full name, phone number, and email. 
Contact objects are stored in a List in temporary storage. The configuration structure includes application properties with main string values, error messages, paths for input/output features, template messages for launcher, and rules for user input validation. 
The application supports two profiles: default and init. The main beans for practice include DefaultUploadServiceImpl.java for launching without external data set uploading and InitUploadServiceImpl.java for launching with data set uploading. 
You can change the profile settings to choose between the two options.

### [Second practice](https://github.com/dmitriy-utkin/spring-framework-practices/tree/main/spring-second-practice-students)
![Static Badge](https://img.shields.io/badge/Spring-shell-blue)
![Static Badge](https://img.shields.io/badge/Docker-builder-red)

The Spring Course Practices project is a console application that incorporates Docker, Spring Shell, and Event Listening. 
It introduces the use of config beans, event listener creation, and containerization with Docker. 
The main configuration points include setting the active profile for the application, enabling event listening, and uploading data. 
The application's model consists of a simple POJO for students, a custom CRUD repository, and custom event realizations. 
The application can be launched using Docker or directly in an IDE. 
The main console commands allow users to add new students, print saved students, and remove students by ID or all at once.

### [Third practice](https://github.com/dmitriy-utkin/spring-framework-practices/tree/main/spring-third-practice-contacts)
![Static Badge](https://img.shields.io/badge/Docker-compose-red)
![Static Badge](https://img.shields.io/badge/Docker-builder-red)
![Static Badge](https://img.shields.io/badge/Spring-data-green)

![Static Badge](https://img.shields.io/badge/Spring-thymeleaf-green)
![Static Badge](https://img.shields.io/badge/Spring-WEB-green)
![Static Badge](https://img.shields.io/badge/Postre-SQL-blue)


The Spring Course Practices project utilizes Docker-compose for PostgreSQL, Spring Boot for the web with MVC architecture, JdbcTemplate for repository implementation, and EventListening. 
The main configuration points include setting the active profile, configuring the database connection, and enabling event listening. 
The application's model consists of a simple POJO for contacts and a custom CRUD repository implemented with JdbcTemplate. 
To launch the application, first, Docker needs to be downloaded and the command "docker compose up" should be run in the "../docker/" directory. 
Once the PostgreSQL database is ready to accept connections, the application can be launched from an IDE.

### [Fourth practice](https://github.com/dmitriy-utkin/spring-framework-practices/tree/main/spring-fourth-practice-news)
![Static Badge](https://img.shields.io/badge/Docker-compose-red)
![Static Badge](https://img.shields.io/badge/Spring-JPA-green)
![Static Badge](https://img.shields.io/badge/Spring-Validation-green)

![Static Badge](https://img.shields.io/badge/Spring-WEB-green)
![Static Badge](https://img.shields.io/badge/Postre-SQL-blue)
![Static Badge](https://img.shields.io/badge/Map-Struct-red)

The Spring Course Practices project introduces several new features, including AOP for user input validation with admin privilege, OpenAPI documentation, Spring Test, Web interceptors, and mapping with MapStruct to transfer Entities <-> DTOs. 
The main configuration points include setting the active profile, configuring the database connection, and enabling event listening. 
The application's model consists of a simple POJO for contacts and a custom CRUD repository implemented with JdbcTemplate. 
To launch the application, Docker needs to be downloaded, and the command "docker compose up" should be run in the "../docker/" directory. Pagination and filtering options are also available for news and users, with default settings and additional filters provided. The project's functionality can be explored further through the OpenAPI web client when the application is running. 
Users can also customize privilege validation and access detailed descriptions of available features through the OpenAPI web client.

### [Fifth practice](https://github.com/dmitriy-utkin/spring-framework-practices/tree/main/spring-fifth-practice-books)
![Static Badge](https://img.shields.io/badge/Spring-cache-green)
![Static Badge](https://img.shields.io/badge/Docker-compose-red)
![Static Badge](https://img.shields.io/badge/Spring-JPA-green)

![Static Badge](https://img.shields.io/badge/Postre-SQL-blue)
![Static Badge](https://img.shields.io/badge/Redis-NoSQL-red)


The book management application is a comprehensive example of leveraging modern technologies to create a full-fledged CRUD application. By incorporating Redis for caching, PostgreSQL for data storage, Docker for deployment, and Testcontainer for testing, the application demonstrates best practices in software development.

### [Sixth practice](https://github.com/dmitriy-utkin/spring-framework-practices/tree/main/spring-sixth-practice-orders-kafka)
![Static Badge](https://img.shields.io/badge/Kafka-7.3.3-orange)
![Static Badge](https://img.shields.io/badge/Zookeeper-7.4.3-orange)

![Static Badge](https://img.shields.io/badge/Docker-compose-red)
![Static Badge](https://img.shields.io/badge/Spring-WEB-green)

Key features of the application include CRUD operations for managing Orders, utilizing a HashMap as an in-memory data store. This approach allows for swift retrieval and manipulation of Order data within the application. Furthermore, integration with Kafka enables the application to communicate asynchronously with other parts of the system. Leveraging Kafka listeners and producers streamlines the consumption and production of messages, ensuring seamless interaction between different components of the application. To simplify the deployment and management of Kafka, the application uses Docker Compose to launch Kafka and Zookeeper services. Introduction to event-driven applications.

### [Seventh practice](https://github.com/dmitriy-utkin/spring-framework-practices/tree/main/spring-seventh-practice-todo)
![Static Badge](https://img.shields.io/badge/Mongo-DB-green)
![Static Badge](https://img.shields.io/badge/Spring_Web-FLUX-green)

![Static Badge](https://img.shields.io/badge/Docker-compose-red)
![Static Badge](https://img.shields.io/badge/Reactive-programming-green)

Introduction to reactive prograaming and NoSQL MongoDB. This appliaction is a TODO storage, you can add/update/delete/get tasks, also make a new users and deligate some task to another user.


### [Eight practice](https://github.com/dmitriy-utkin/spring-framework-practices/tree/main/spring-eights-practice-spring-security)
Was implemented Spring Security features to the fourth and seventh practices
#### [Updated 4th practice](https://github.com/dmitriy-utkin/spring-framework-practices/tree/main/spring-eights-practice-spring-security/spring-security-news)
#### [Updated 7th practice](https://github.com/dmitriy-utkin/spring-framework-practices/tree/main/spring-eights-practice-spring-security/spring-security-todo)
