# SPRING COURSE EIGHT (8.2) PRACTICE

New features: reactive programming paradigm with using Mono/Flux, MongoDb.
___

Good day to everyone! :wave:

___

## Practice #8 tasks:

- Implementation of Security service to #4 practice.

___
___
## New features:
Spring Security implementation and the following rules for 3 roles (ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR):

___

### PublicController

- Any user can create an account

___

### UserController

#### ROLE_USER can:

- Get all or exact user by id/username
- Update user`s info
- Delete user

#### ROLE_MANAGER can:

- Get all or exact user by id/username
- Update user`s info
- Delete user
- Create new user

___

### TaskController

#### ROLE_USER can:

- Get all or exact task by id
- Add observers to task

#### ROLE_MANAGER can:

- Get all or exact task by id
- Add observers to task
- Create task
- Delete task
- Update task

___
___

## README.md from the 7th practice and main info:


## Practice 7th tasks:
* Introduction to reactive programming
* Using Mono and Flux in the controllers
* Creation STREAM for new data (based on ServerSentEvent, reactive programming)
* Tests for application

## Main configuration points:

### Database
Below the main settings for using of database (MongoDB), nothing special.
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://root:root@localhost:27017/tododatabase?authSource=admin
```

### Listening and autofilling the database
Below you can find settings for turn on or off the automatic creation of users into the database
```yaml

app:
  listening:
    enable: false
```

### User authentication
And last on is the settings for user who will launch this application. This data is using when you add new task (author ID)
```yaml
app:
  user:
    username: dmitriy_un
    email: initial.author@gmail.com
```

## How launch this application:
1. First of all you need to clone this repository to your local machine
2. Then you need to launch Docker
3. When two previous points done, please go into the docker directory and run `docker compose up`
4. Then you can start this application on your local machine

## How does it work:
Application have few end points:

### Users part: `http://localhost:8080/api/user`
* GET
  * get all users
  * `.../{userId}` - to get user by id
  * `.../username/{username}` - to get user by username
* POST
  * to create a new user (username:String, email:String)
* PUT
  * `.../{userId}` - to update some user
* DELETE
  * `.../{userId}` - to delete some user
  

### Tasks part: `http://localhost:8080/api/task`
* GET
  * get all tasks
  * `.../{taskId}` - to get task by id
* POST
  * to create a new task (name:String, description:String, status:TaskStatus, assigneeId:String (optional), observerIds:Set<String> (optional))
* PUT
  * `.../{taskId}` - to update some task
  * `.../observers/{taskId}` - to add new observer to the task, you need to add parameter: observerId:String
* DELETE
  * `.../{taskId}` - to delete some task

### Stream part: `http://localhost:8080/api/stream`
* GET
  * `.../user` - to open a stream for new users
  * `.../task` - to open a stream for new tasks