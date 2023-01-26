# Messaging service

## Technology

* Java 17
* Spring boot 3.0.2
* H2 in memory database
* RestAssured & JUnit5

## Build & Run - localhost:8080

#### You will need either Docker or Java 17 and Maven installed. Both options below run unit tests

### If using maven

* mvn clean install spring-boot:run

### If using docker

Docker will build and run the application, there is no need to pre-build jar file

* Navigate to root directory of project and run below commands (where the dockerfile is)
* docker build -t messagingservice .
* docker run -p 8080:8080 messagingservice