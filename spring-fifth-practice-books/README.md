# SPRING COURSE FIFTH PRACTICE

New features: Redis for caching of the response (like books by name, name+author, id, categories) 
with eviction when update/delete/create any antities. Also added test with using of the Testcontainer. 
Implemented RESTful paradigm with additional end points for testing some features, 
docker-compose for convenient launch of third-party services (like Redis, PostgreSQL).

___

Hello :wave:

___

## Practice 5th tasks:
* CRUD application for manage of the books (create new one, update existed, delete, find all or part by exact request) with MVC and RESTful principles. 
* Development with Redis. 
* Data caching in Redis.
* Implementation Testcontainers

## The main configuration points:

### JPA Settings
`spring.jpa` & `spring.datasource` - the default setting to connect to database with help of docker (was used a PostgreSQL)
```yaml
spring:
  jpa:
    generate-ddl: true
    show-sql: true
  datasource:
    url: jdbc:postgresql://localhost:5432/books_db
    username: postgres
    password: postgres
    hikari:
      schema: books_schema
      connection-timeout: 10000
```
### Uploading Settings
`app.upload` - here you can turn off or on the listener for upload the generic data to database, also you can change the number of default books (with fields like "book1", "author1" and etc.")
```yaml
app:
  upload:
    enable: true
    dataPath: src/main/resources/data/books-mock-data.json
    defaultBooksNumber: 20
```

### Cache Settings
`app.cache` - here you can find the main settings for caching of the entities in this application.
```yaml
app:
  cache:
    enable: true
    cacheType: redis
    cacheNames:
      - dbBooks
      - dbBooksByCategory
      - dbBookByNameAndAuthor
      - dbBookById
      - dbBookByName
      - dbBooksByAuthor
    properties:
      dbBooks:
        expiry: 3m
#        ...
```

## How launch this application?

You need clone this application to your local repository or download it from GitHub. 
Then you need to launch the Docker, write command to console in docker directory `docker compose up` and run this application. 
Default values in the application.yaml will give you possibility to use it from box. 

## What can you do here?

Basically you can use a Postman to test this application for the following features:

BASE URL: http://localhost:8080/api/book

* `findAll(@RequestBody FindAllSettings)` by get request to base url with body (used default values with help of @Builder.Default):
```json
    {
      "pageSize": 20,
      "pageNum": 0,
      "filter": {
        "bookName": null,
        "categoryName": null,
        "categoryId": null,
        "author": null
      }
    }
```

* `findBookByName(@PathVariable String name)` by get request to base url with path variable `/{name}`
* `findBookById(@PathVariable Long id)` by get request to base url with `/id` and path variable `/{id}`
* `findBooksByAuthor(@PathVariable String author)` by get request to base url + `/author/{author}`
* `searchBookByCategory(@RequestParam("category") String category)` by get request to base url + `/search/category` with param category
* `findBookByBookNameAndAuthor(@RequestParam("name") String name, @RequestParam("author") String author)` by get request to base url + `/search` and params author, name (bookName)
* and other CRUD default methods (create, update, delete)

## Model for database and web

### Database model
```java
public class Book implements Serializable {
    private Long id;
    private String name;
    private String author;
    private Category category;
}

public class Category implements Serializable {
    private Long id;
    private String name;
    private List<Book> books = new ArrayList<>();
}
```
### Web model
```java
public class BookResponse {
    private Long id;
    private String author;
    private String name;
    private String category;
}

public class BookListResponse {
    private List<BookResponse> books = new ArrayList<>();
}

public class UpsertBookRequest {
    private String author;
    private String name;
    private String category;
}
```

## How caching with Redis is working?
Beans was created in Configuration class `RedisConfig` & `CacheConfig` (properties in class `AppCacheProperties` with link to application.yaml settings)

The main work of caching is realized on Service layer (BookServiceImpl). The cache is created in the get methods and evicted in the post/put/delete methods. The details you can find in the service class.

## How Testcontainers is working?
This application contains the test for service/repo/controller layers with help of @Testcontainers. Here was implemented WireMock features for each of request to seerver, also created Redis and Postgres testContainers for integration tests.
