# SPRING COURSE SECOND PRACTICE

Docker, Spring-Shell and a little EventListening
___

Hey everyone :wave:

### Practice #2 tasks:
* Spring shell application introduction
* Using and setting up of the config beans
* Event listener using and creation
* Introduction to the Docker a containerization of my app (with the following using it in the console)

## The main configuration points

```yaml
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:prod}

app:
  listening:
    enable: ${APP_LISTENING_ENABLE:true}

  uploading:
    enable: ${APP_UPLOADING_ENABLE:true}
    path: ${APP_UPLOADING_PATH:src/main/resources/students_mock.csv}
```

* `spring.profile.active:` - is a profile for running an application, here you can put test/default/prod,
only prod profile contain EvenListener logic, in the another option you will get smth like "under construction"
* `app.listening.enable` - just use "true" if you want to check a EventListener option (btw - bean 
"EventListenerService" will be created in the "test" profile, but a logic in this service will be limited)
* `app.uploading.enable` - if you want to upload prepared info-set to the repo, put "true" (the next one (`app.uploading.path`) is a path of file with that data)

So, here you can see prepared a template for Docker image (link to the ENV variables), I will describe it in the following part of README.md. 

## Model & repo & events
___Model___ (`com.spring.secondPractice.model.Student`) -> is a simple POJO with field firstName, lastName, age and id (its generated automatically)

___Repo___ (`com.spring.secondPractice.repositories.StudentRepositoryImpl`) -> is a custom realisation of CRUD repository

___Events___(`com.spring.secondPractice.events.*`) -> the same things: here you can find a custom realisation of Events for each of shell-methods

## How launch this application?

### First way: Docker
If you have installed Docker application, you can you the following command in the console:

`1> docker build -t <your_image_name> .`

`2> docker run -it --rm -e <some_of_env>=<env_value> <your_image_name>` -- where:

* -it - to launch Spring Shell application with console interface
* --rm - to delete a container when application will be finished
* -e - internal environments of this application, you can change each of these:
  * SPRING_PROFILES_ACTIVE - to choose a profile when you are running this app
  * APP_LISTENING_ENABLE - to turn on or off the listeners
  * APP_UPLOADING_ENABLE - to allow or denied uploading from prepared source
    ```yaml
    ENV SPRING_PROFILES_ACTIVE=prod
    ENV APP_LISTENING_ENABLE=true
    ENV APP_UPLOADING_ENABLE=true
    ```

### Easy but boring way: just to run in the IDE
Just download this project from GitHub and click "run" :)


## Main console command:

* help: Display help about available commands 
* clear: Clear the shell screen 
* quit, exit: Exit the shell
* history: Display or save the history of previously run commands
* add: Add a new students (parameters: firstName lastName age)
* print: Print saved students
* rmById: Remove by student id 
* rmAll: Remove all students