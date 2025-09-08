# Music Library

Technologies used:

- Spring Boot 3.2.0
- Spring Data
- Docker and docker-compose
- Lombok
- Postgres SQL

To run this application follow the steps bellow:

## 1. Run the integration tests

```bash
mvn verify
```

## 1. Start the Application

```bash
# Build the application
mvn clean package -DskipTests

# Start all services including Redis
docker-compose down --rmi all && docker-compose up --build -d

# Check that all containers are running
docker-compose ps
```
Or use:
```bash
# Build the application
mvn clean package -DskipTests

# Remo all images 
docker-compose down --rmi all

# Start all services including Redis not detached
docker-compose up --build
```

Rockstars-Music-Library-JWT-API.postman_collection.json

Go to: http://localhost:8080/actuator/health, and check whether the application is running!

Find the api definition here: http://localhost:8080/swagger-ui/index.html#/
