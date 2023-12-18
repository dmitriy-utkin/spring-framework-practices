# SPRING COURSE THIRD PRACTICE

Docker-compose for PostgreSQL, Spring-boot for WEB (MVC), JdbcTemplate for repository implementation and a little EventListening

___

Hello :wave:

## Practice #3 tasks:

* Spring MVC architectural pattern introduction
* JdbcTemplate introduction
* Using docker compose to run a psql server and 'init' it
* A little web-interface introduction

## The main configuration points:

* `spring.profiles.active` - profile setting, no any features variables and main functional is available in default/prod profiles
* `spring.datasource.`
  * `...url` - url for your database and driver to connect
  * `...username` & `...password` - username & password to get a privilege in the db
  * `...hokari.schema` - a db schema name
* `app.input.`
  * `...enable` - true/false setting for using of the listening service (if you put "true" - the application will add N contacts to the data base)
  * `...numberOfGenericContacts` - the exact number of generic contacts creation (N from the last on)


```yaml
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:default}

  datasource:
    url: ${PSQL_URL:jdbc:postgresql://localhost:5432/contacts}
    username: ${PSQL_USER:postgres}
    password: ${PSQL_PASS:postgres}
    hikari:
      schema: ${PSQL_SCHEMA:contacts_schema}

app:
  input:
    enable: ${APP_UPLOADING_ENABLE:true}
    numberOfGenericContacts: ${DEFAULT_NUMBER_GEN_CONTACT:10}
```

## Model & repo

### Model:

Here is a very simple POJO contact with comparator by ID field:
```java
    public class Contact implements Comparable<Contact> {
        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
    }
```

### Repo ___(was implemented with IdbcTemplate):___

The main features:
```java
    public interface ContactRepository {
        List<Contact> findAll();
        Optional<Contact> findById(int id);
        void save(Contact contact);
        void update(Contact contact);
        void deleteById(int id);
        void batchInsert(List<Contact> contacts);
        int count();
        int maxId();
        void deleteAll();
        boolean existsById(int id);
    }
```

## How launch this application?

### Step one

You need to download a Docker to your device and go into the directory "`../docker/`"

Than you need to run the following command:
`docker compose up`

### Step two

When you will see "`LOG:  database system is ready to accept connections`" in your console, you need just launch this application from your IDE