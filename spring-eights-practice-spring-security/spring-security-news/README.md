# SPRING COURSE EIGHT (8.1) PRACTICE

New features: Spring Security 

___

Hello everyone :wave:

---

### _You can find the controller full documentation on [OpenAPI web client](http://localhost:8080/swagger-ui/index.html) when application is started_

## Practice #8 tasks:

- Implementation of Security service to #4 practice.

___
___
## New features:
Spring Security implementation and the following rules for 3 roles (ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR).
All internal validations (like check is it an admin/moderator or not, is it an owner of this entity) realised with AOP.

___

### PublicController

- Any user can create an account

___

### UserController

#### ROLE_USER can:

- Get info about user by ID (but only about yourself)
- Update information about user (but only yourself)
- Delete user`s account (but only yourself)

#### ROLE_ADMIN can:

- Get information about all users
- Get info about user by ID
- Update information about user
- Delete user`s account
- Create new user

#### ROLE_MODERATOR can:

- Get info about user by ID
- Update information about user
- Delete user`s account

___

### NewsController

#### ROLE_USER can:

- Get info about all news or exact news by id
- Create news
- Update news (but only the news that was created by yourself)
- Delete news (but only the news that was created by yourself)

#### ROLE_ADMIN can:

- Get info about all news or exact news by id
- Create news
- Update news (but only the news that was created by yourself)
- Delete any news

#### ROLE_MODERATOR can:

- Get info about all news or exact news by id
- Create news
- Update news (but only the news that was created by yourself)
- Delete any news

___

### TopicController

#### ROLE_USER can:

- Get info about all topics or exact topic by id

#### ROLE_ADMIN can:

- Get info about all topics or exact topic by id
- Create new topic
- Update existed topic
- Delete topic by id

#### ROLE_MODERATOR can:

- Get info about all topics or exact topic by id
- Create new topic
- Update existed topic
- Delete topic by id
___

### CommentController

#### ROLE_USER can:

- Create new comment
- Get all or exact comment by id
- Update comment (but only the comment that was created by this user)
- Delete comment (but only the comment that was created by this user)

#### ROLE_ADMIN can:

- Create new comment
- Get all or exact comment by id
- Update comment (but only the comment that was created by this user)
- Delete comment

#### ROLE_MODERATOR can:

- Create new comment
- Get all or exact comment by id
- Update comment (but only the comment that was created by this user)
- Delete comment

___
___

## README.md from the 4th practice and main info:

## Practice #4 tasks:

- Development of REST API using Spring MVC.
- Validation of incoming client requests.
- AOP (Aspect-Oriented Programming).
- Creation of application layer for database operations using Spring Boot Data JPA.
- Writing specifications for filtering news during requests.
- Entity mapping using MapStruct.

## The main configuration points:

### Spring settings:

- `profiles.active:` - you can find the exact profile for ypu application. I recommend to use a 'prod' one.
- `jpa:...` - setting for connection and usage of database, default settings will give you connection to the PostgreSQL.

```yaml
spring:
  profiles:
    active: prod
  jpa:
    generate-ddl: true
    show-sql: true
  datasource:
    url: jdbc:postgresql://localhost:5432/news_db
    username: postgres
    password: postgres
    hikari:
      schema: news_schema
      connection-timeout: 10000
```
### Application validator settings:
- `app.validation.enable` - to turn off or turn on the validator in the application
- `app.user-settings` - settings for your account name, header for all the HTTP request in this application 

```yaml
app:
  validation:
    enable: true
  user-settings:
    userName: dmitriy
    userHeader: X-App-User
```
### Settings for uploading generic data to the database
- `app.generic-data.enable` - to turn off or on this features
- `app.generic-data.multiplier` - to choose how many times the set of comments will be repeated during uploading the data to DB
- `app.generic-data.news/users/comments...DataPath` - the location of files with generic data

```yaml
app:
  generic-data:
    enable: true
    multiplier: 3
    newsDataPath: src/main/resources/data/news-data-set.json
    usersDataPath: src/main/resources/data/users-data-set.json
    commentsDataPath: src/main/resources/data/comments-data-set.json
```

## How launch this application?

1) Firstly you need to clone this repository to your local repository
2) Then open it in IDEA (or your variant)
3) The next step is make sure the Docker application is running and have a feature of Docker-compose
4) Then you need to run docker compose file (in the `docker` directory write command `docker compose up`)
5) When server ready, you need to add your username to configuration file: `app.user-settings.userName`
 (default - admin, you will find the description of this features below)
6) And finally lets run application!

_Please pay attention: you can turn off any privilege validation in the configuration file: `app.validation.enable: false`_

## What can you do here?
1) Manage users (with privilege restrictions)
2) Manage news (with privilege restrictions)
3) Manage comments (with privilege restrictions)
4) Manage topics (with privilege restrictions)

A little more about findAll() -> here was implemented pagination for News and Users. Default values for pageNum - 0, pageSize - 10 (implemented by Lombok project):

```java
public class FindAllSettings {

    @Builder.Default
    private Integer pageSize = 10;

    @Builder.Default
    private Integer pageNum = 0;

    @Builder.Default
    private NewsFilter filter = new NewsFilter();


}
```


Also, here you can find filters with help of JPA specification for News (if you will not to use any setting for this filter, you will get all the news from the DB):

```java
public class NewsFilter {

    private Long topicId;

    private Long userId;

    private String userName;

    private String topic;
}
```

All these topics available by RESTful logical, you can find the exact description all of them on [OpenAPI web client](http://localhost:8080/swagger-ui/index.html) (only when app is started!)

## How validation of privilege is working?

Firstly, validation is working with AOP layer by annotation `@PrivilegeValidation`

### Users manage validation
1) You can change only yours user details (username, email)
2) You cant delete your account or account other users
3) You cant save new user. new user can be created by admin or on starting the application

#### Admin privilege:
- admin can change, save and delete any accounts, except of `admin`

### News manage validation
1) You can change or delete only your news

#### Admin privilege:
- admin can change any news, all the changed by admin entities will have a prefix "(Upd by admin)" in the news title

### Comments manage validation
1) You can change or delete only your comments

#### Admin privilege:
- admin can change any comments, all the changed by admin entities will have a prefix "(Upd by admin)" in the comment body

### Topics manage validation
- only admin can change or delete topics