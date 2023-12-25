# SPRING COURSE SIXTH PRACTICE

New features: kafka integrated for listening of events from first application, 
process in the second one and sending updated event back.

___

Hello everyone :wave:

___

Practice 6th tasks:

* Setting up Kafka in applications.
* Sending messages to Kafka.
* Receiving and processing messages from Kafka.

## Structure:

This project contains two application to get a practice experience in Kafka and message broking.
* First application (order-service) - have a simple service (save/update/findAll) and two end points
  * 1st end point - POST to `http://localhost:8080/api/order` with an order body
  * 2nd end point - GET to the same URL, to get a list of orders (for example, to check th correct answer from the second one)
* Second application (order-status-service) - only kafka listener and sender features

## The main configuration points for both of application in this project:

The same for both of application:
* `spring.kafka.bootstrap-servers` - address for bootstrap kafka server
* `app.kafka.kafkaGroupId` - group id for both of application
* `app.kafka.serviceTopic` - topic where sending information about creation of order to
* `app.kafka.statusTopic` - topic where sending order with changed status
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092

app:
  kafka:
    kafkaGroupId: "kafka-order-group-id"
    serviceTopic: "order-topic"
    statusTopic: "order-status-topic"
```
For order-service:
* Port for launching application
```yaml
server:
  port: 8080

```
For order-status-service:
* Port for launching application
```yaml
server:
  port: 8082
```

## How launching these application:
1) First of all you need to clone this repository to your local machine
2) Then you need to launch Docker
3) Write script in docker directory of order-service: `sh docker-start.sh` (for MacOS) or `cmd docker-start.cmd` (for Windows)
4) Then you need launch both application and go to the Postmane to check it
   * You can try to make a POST request to `http://localhost:8080/api/order` with body like `{"product":"Product 1", "quantity":1}`

## How does it work:
1) You send POST request to `http://localhost:8080/api/order` with order body
2) Application `order-service` send a event to kafka by template `{"date": Instant.now(), "order": Order}` to topic `order-topic`
3) Application `order-status-service` listen the `order-topic`, when its get some message from kafka by this topic, 
second application change an order status (set random value from the StatusOrder options)
4) Then second application post a new event to topic `order-status-topic` with new date
5) First application listen the topic `order-status-topic`, when its get a new message from kafka, listener update an order entity

Also each of event in kafka listeners send a logs to console when receive any events.

To be sure that the 1st app sent event and 2nd get it, change and set back, you can make a GET request to `http://localhost:8080/api/order`.
Note: the default value for Order Status is CREATED, second application can set any statuses instead of this one.