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
It also utilizes JPA specifications and Docker-compose for PostgreSQL. 
The main configuration points include setting the active profile, configuring the database connection, and enabling event listening. 
The application's model consists of a simple POJO for contacts and a custom CRUD repository implemented with JdbcTemplate. 
To launch the application, Docker needs to be downloaded, and the command "docker compose up" should be run in the "../docker/" directory. 
Once the PostgreSQL database is ready to accept connections, the application can be launched from an IDE. 
The project also provides detailed instructions for setting up and launching the application, including managing users, news, comments, and topics with privilege restrictions. 
Pagination and filtering options are also available for news and users, with default settings and additional filters provided. The project's functionality can be explored further through the OpenAPI web client when the application is running. 
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

Key features of the application include CRUD operations for managing Orders, utilizing a HashMap as an in-memory data store. This approach allows for swift retrieval and manipulation of Order data within the application. Furthermore, integration with Kafka enables the application to communicate asynchronously with other parts of the system. Leveraging Kafka listeners and producers streamlines the consumption and production of messages, ensuring seamless interaction between different components of the application. To simplify the deployment and management of Kafka, the application uses Docker Compose to launch Kafka and Zookeeper services. This approach guarantees easy access to and proper configuration of Kafka and Zookeeper for use within the application. Configuration parameters for Kafka are set via @Bean annotations, providing a clear and concise way to define the necessary configurations for interacting with Kafka. This empowers developers to tailor Kafka parameters according to their specific requirements while maintaining clean and organized code. In conclusion, our Spring MVC application with integrated Kafka functionality offers a robust solution for managing Orders and provides valuable experience in working with Kafka. With its seamless integration of an internal data store, Kafka message handling, and Docker-based deployment, this project serves as an excellent foundation for developers seeking practical experience with these technologies and aiming to build scalable, event-driven applications.
