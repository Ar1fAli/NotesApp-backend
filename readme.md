# NotesApp Backend

A robust and scalable backend for a note-taking application, built with Spring Boot. This service provides user authentication, note creation, retrieval, updating, and deletion through a RESTful API, using PostgreSQL for data storage and JWT for secure authentication.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Environment Variables](#environment-variables)
  - [Running the Application Locally](#running-the-application-locally)
  - [Running with Docker](#running-with-docker)
- [API Endpoints](#api-endpoints)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## Features

- **User Authentication**: Secure user registration and login using JWT.
- **CRUD Operations**: Create, read, update, and delete notes for authenticated users.
- **RESTful API**: Well-structured endpoints for seamless frontend integration.
- **Database**: PostgreSQL for reliable relational data storage.
- **Scalable Architecture**: Built with Spring Boot for high performance and modularity.
- **Docker Support**: Containerized setup for consistent development and deployment.

## Tech Stack

- **Framework**: Spring Boot (Java)
- **Database**: PostgreSQL
- **Authentication**: Spring Security with JSON Web Tokens (JWT)
- **Build Tool**: Maven
- **Containerization**: Docker
- **Runtime**: Eclipse Temurin 17 JRE
- **Other Dependencies**: Spring Data JPA, Hibernate (PostgreSQL dialect)

## Getting Started

### Prerequisites

Ensure you have the following installed:

- [Java](https://www.oracle.com/java/) (JDK 17 or higher)
- [Maven](https://maven.apache.org/) (version 3.9.6 or compatible)
- [PostgreSQL](https://www.postgresql.org/) (local or cloud-based)
- [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) (for containerized setup)
- A code editor like [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [VS Code](https://code.visualstudio.com/)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Ar1fAli/NotesApp-backend.git
   cd NotesApp-backend
   ```

2. Install dependencies using Maven:
   ```bash
   mvn clean install
   ```

### Environment Variables

Create a file named `application.properties` in `src/main/resources` and configure the following environment variables. Alternatively, set these as system environment variables or provide them via Docker.

**Example `application.properties`:**

```properties
spring.application.name=notesapp
spring.datasource.url=jdbc:postgresql://localhost:5432/notesapp
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000 # Token expiration in milliseconds (e.g., 24 hours)
```

### Running the Application Locally

1. Ensure PostgreSQL is running locally or in a cloud instance:

   ```bash
   psql -U your_postgres_username -d postgres
   CREATE DATABASE notesapp;
   ```

2. Run the Spring Boot application:

   ```bash
   mvn spring-boot:run
   ```

3. The server will start at `http://localhost:8080` (default port, configurable via `server.port`).

### Running with Docker

Docker is recommended for consistent development and deployment environments. The provided `Dockerfile` uses a multi-stage build to compile the application with Maven and run it with Eclipse Temurin 17 JRE. Below are detailed steps to set up and run the application using Docker and Docker Compose.

#### Step 1: Verify the `Dockerfile`

Your `Dockerfile` is already well-structured for building and running the application:

```dockerfile
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
COPY --from=build /app/target/notesapp-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

- **Build Stage**: Uses `maven:3.9.6-eclipse-temurin-17` to build the JAR, skipping tests for faster builds.
- **Runtime Stage**: Uses `eclipse-temurin:17-jre` for a lightweight runtime, copying the built JAR (`notesapp-0.0.1-SNAPSHOT.jar`).

#### Step 2: Create a `docker-compose.yml`

To run the application and PostgreSQL together, create a `docker-compose.yml` file in the project root:

```yaml
version: "3.8"

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/notesapp
      - SPRING_DATASOURCE_USERNAME=${DB_USERNAME}
      - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
      - SPRING_JPA_HIBERNATE_DDL_AUTO=${JPA_DDL_AUTO}
      - SPRING_JPA_SHOW_SQL=true
      - SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
      - JWT_SECRET=${JWT_SECRET}
      - JWT_EXPIRATION=${JWT_EXPIRE}
    depends_on:
      - db
    networks:
      - notesapp-network

  db:
    image: postgres:15
    environment:
      - POSTGRES_DB=notesapp
      - POSTGRES_USER=${DB_USERNAME}
      - POSTGRES_PASSWORD=${DB_PASSWORD}
    ports:
      - "5432:5432"
    volumes:
      - db-data:/var/lib/postgresql/data
    networks:
      - notesapp-network

networks:
  notesapp-network:
    driver: bridge

volumes:
  db-data:
```

#### Step 3: Set Environment Variables for Docker

Create a `.env` file in the project root to provide the environment variables used in `docker-compose.yml`:

```env
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password
JPA_DDL_AUTO=update
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRE=86400000
```

**Security Note**: Do not commit the `.env` file to version control. Add it to `.gitignore` to protect sensitive information like `DB_PASSWORD` and `JWT_SECRET`.

#### Step 4: Build and Run with Docker Compose

1. Ensure Docker and Docker Compose are installed and running.
2. Build and start the services:

   ```bash
   docker-compose up --build
   ```

3. The application will be accessible at `http://localhost:8080`, and the PostgreSQL database will be available at `localhost:5432`.

4. To stop the services:

   ```bash
   docker-compose down
   ```

5. To stop and remove volumes (clear database data):
   ```bash
   docker-compose down -v
   ```

#### Step 5: Running the Docker Image Standalone (Optional)

If you prefer to run only the application container (e.g., with an external PostgreSQL instance):

1. Build the Docker image:

   ```bash
   docker build -t notesapp-backend .
   ```

2. Run the container, passing environment variables:
   ```bash
   docker run -p 8080:8080 \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/notesapp \
     -e SPRING_DATASOURCE_USERNAME=your_postgres_username \
     -e SPRING_DATASOURCE_PASSWORD=your_postgres_password \
     -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
     -e SPRING_JPA_SHOW_SQL=true \
     -e SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect \
     -e JWT_SECRET=your_jwt_secret_key \
     -e JWT_EXPIRATION=86400000 \
     notesapp-backend
   ```

#### Docker Notes

- **Database Persistence**: The `db-data` volume in `docker-compose.yml` ensures PostgreSQL data persists between container restarts. Use `docker-compose down -v` to clear it if needed.
- **Port Conflicts**: If ports `8080` or `5432` are in use, update the `ports` mapping in `docker-compose.yml` (e.g., `"8081:8080"`).
- **Skipping Tests**: The `Dockerfile` skips tests (`-DskipTests`) for faster builds. If tests are required, remove `-DskipTests` or add a separate build step.
- **Environment Variables**: Ensure all variables (`DB_USERNAME`, `JWT_SECRET`, etc.) are set in the `.env` file or passed directly to `docker run`.
- **Docker Hub**: To share the image, push it to Docker Hub:
  ```bash
  docker tag notesapp-backend your_dockerhub_username/notesapp-backend:latest
  docker push your_dockerhub_username/notesapp-backend:latest
  ```

## API Endpoints

Below are example API endpoints (based on typical Spring Boot notes app implementations):

| Method | Endpoint             | Description                    | Authentication |
| ------ | -------------------- | ------------------------------ | -------------- |
| POST   | `/api/auth/register` | Register a new user            | None           |
| POST   | `/api/auth/login`    | Log in and receive a JWT token | None           |
| GET    | `/api/notes`         | Retrieve all notes for a user  | JWT            |
| POST   | `/api/notes`         | Create a new note              | JWT            |
| PUT    | `/api/notes/{id}`    | Update a note by ID            | JWT            |
| DELETE | `/api/notes/{id}`    | Delete a note by ID            | JWT            |

To test the API, use tools like [Postman](https://www.postman.com/) or [curl](https://curl.se/). If Swagger/OpenAPI is enabled, access the documentation at `http://localhost:8080/swagger-ui.html`.

## Deployment

For production deployment, consider the following options:

1. **Local JAR Deployment**:
   - Build the JAR:
     ```bash
     mvn package
     ```
   - Run the JAR with environment variables:
     ```bash
     java -jar -Dspring.datasource.url=jdbc:postgresql://host:5432/notesapp \
       -Dspring.datasource.username=your_username \
       -Dspring.datasource.password=your_password \
       -Djwt.secret=your_secret \
       target/notesapp-0.0.1-SNAPSHOT.jar
     ```

2. **Docker Deployment**:
   - Use the `docker-compose.yml` above for local or cloud deployment.
   - Deploy to platforms like AWS ECS, Kubernetes, or Render, ensuring the PostgreSQL database is accessible.
   - Example for Kubernetes: Create a deployment YAML referencing the Docker image and set environment variables via a ConfigMap or Secret.

3. **Cloud Platforms**:
   - Deploy to Heroku, AWS Elastic Beanstalk, or Render.
   - Configure environment variables (`DB_URL`, `JWT_SECRET`, etc.) in the platform’s dashboard.
   - Use a managed PostgreSQL service (e.g., AWS RDS, Heroku Postgres) for the database.

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request.
