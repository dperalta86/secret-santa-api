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

## ⚙️ Configuration

This project uses Spring Profiles for different environments:

- **dev**: Local development (uses Docker MySQL)
- **prod**: Production deployment

### Local Development Setup

1. **Start the database**:

```bash
docker compose up -d
```

2. **Configure environment** (optional for email testing):

```bash
cp application-example.yml .env
# Edit .env with your credentials (if testing emails)
```

3. **Run the application**:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Or from IntelliJ: Set active profile to `dev` in Run Configuration.

### Production Deployment

1. Set environment variables on your hosting platform
2. Ensure `SPRING_PROFILES_ACTIVE=prod`
3. Configure database connection to your MySQL instance

**Required environment variables for production**:

- `DB_URL`: Your MySQL connection string
- `DB_USERNAME`: Database user
- `DB_PASSWORD`: Database password
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`: SMTP config
- `FRONTEND_URL`: Your frontend URL

See `application-example.yml` for all available configuration options.

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
