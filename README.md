# 🎁 Secret Santa API

REST API for managing Secret Santa draws with automated email notifications.

## 🚀 Tech Stack

- **Java 21**
- **Spring Boot 4.1.0**
- **MySQL 8.0**
- **Flyway** for database migrations
- **Docker & Docker Compose**
- **Maven**

## 🏃 Quick Start

1. Clone the repository

```bash
git clone https://github.com/dperalta86/secret-santa-api.git
cd secret-santa-api
```

2. Start MySQL with Docker Compose

```bash
docker compose up -d
```

3. Run the application

```bash
./mvnw spring-boot:run
```

4. Access the API

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Database Admin: http://localhost:8081

## 📊 Database Schema

The application uses two main tables:

- `draws`: Secret Santa events
- `participants`: People participating in each draw

## 🔜 Roadmap

- [ ] Core API endpoints
- [ ] Draw algorithm implementation
- [ ] Email notification service
- [ ] Frontend interface
- [ ] Unit & Integration tests
- [ ] CI/CD pipeline
