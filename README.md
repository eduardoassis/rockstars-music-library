# Music Library

Technologies used:

- Spring Boot 3.2.0
- Spring Data MongoDB
- Lombok
- Postgres SQL

To run this application follow the steps bellow:

First run the integration tests:

```shell
$ mvn verify
```


First go to the main directory of the application then run the commands:

```shell
$ mvn package
```

After creating the application run:

```shell
$ docker-compose build
$ docker-compose up
```

Go to: http://localhost:8080/actuator/health, and check whether the application is running!

Find the api definition here: http://localhost:8080/swagger-ui/index.html#/
