[![PT](https://img.shields.io/badge/🇧🇷-Português-2ea44f?style=for-the-badge)](./README.md)
[![EN](https://img.shields.io/badge/🇺🇸-English-0A66C2?style=for-the-badge)](./README.en.md)

---

# 📅 Appointment Booking API

[![Java](https://img.shields.io/badge/Java%2021-ED8B00?logo=java&logoColor=white)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-000000?logo=jsonwebtokens&logoColor=white)](https://jwt.io)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?logo=postgresql&logoColor=white)](https://www.postgresql.org)
[![Flyway](https://img.shields.io/badge/Flyway-CC0200?logo=flyway&logoColor=white)](https://flywaydb.org)
[![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black)](https://swagger.io)

> REST API for appointment management with JWT authentication, role-based access control and real business rules.

---

## About

The **Appointment Booking API** simulates a real-world service scheduling system. The project was built focusing on backend best practices: JWT security, layered architecture, data validation and global exception handling.

---

## API Flow

```
Client
  │
  ▼
[JwtAuthenticationFilter] → validates Bearer token on every request
  │
  ▼
[SecurityConfig] → checks ROLE (CLIENT / ADMIN)
  │
  ├── POST /auth/**              → public (register, login)
  ├── POST /appointments         → CLIENT only
  ├── GET  /appointments/me      → CLIENT only
  ├── GET  /appointments         → ADMIN only
  └── DELETE /appointments/{id}  → CLIENT (own) | ADMIN (any)
  │
  ▼
Controller → Service → Repository → PostgreSQL
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| ORM | JPA / Hibernate |
| Migrations | Flyway |
| Build | Maven |
| Documentation | SpringDoc OpenAPI (Swagger UI) |

---

## Roles & Permissions

| Endpoint | CLIENT | ADMIN |
|---|:---:|:---:|
| POST /auth/register | ✅ | ✅ |
| POST /auth/login | ✅ | ✅ |
| POST /appointments | ✅ | ❌ |
| GET /appointments/me | ✅ | ❌ |
| GET /appointments | ❌ | ✅ |
| DELETE /appointments/{id} | own only | ✅ |

---

## Business Rules

- Appointments cannot be scheduled in the past
- Time slot conflicts are not allowed
- CLIENT can only access and cancel their own appointments
- ADMIN can access and cancel any appointment

---

## Running the project

### Prerequisites

- Java 21
- PostgreSQL running locally
- Maven

### 1. Clone the repository

```bash
git clone https://github.com/FL4V10ARC/appointment-booking-api.git
cd appointment-booking-api
```

### 2. Create the database

```sql
CREATE DATABASE appointment_booking;
```

### 3. Configure `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/appointment_booking
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

---

## Endpoints

### Authentication

```bash
# Register
POST /auth/register
{
  "name": "Flávio",
  "email": "flavio@email.com",
  "password": "123456",
  "role": "CLIENT"
}

# Login → returns JWT
POST /auth/login
{
  "email": "flavio@email.com",
  "password": "123456"
}
```

### Appointments

```bash
# Create appointment (CLIENT)
POST /appointments
Authorization: Bearer <token>
{
  "appointmentTime": "2025-12-01T10:00:00"
}

# My appointments (CLIENT)
GET /appointments/me
Authorization: Bearer <token>

# All appointments (ADMIN)
GET /appointments
Authorization: Bearer <token>

# Cancel appointment
DELETE /appointments/{id}
Authorization: Bearer <token>
```

---

## Interactive Documentation (Swagger)

With the application running, access:

```
http://localhost:8080/swagger-ui/index.html
```

To test authenticated endpoints:
1. Login at `POST /auth/login`
2. Copy the returned token
3. Click **Authorize** in Swagger
4. Paste: `Bearer YOUR_TOKEN`

---

## Author

**Flávio Carvalho**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Flávio%20Carvalho-0A66C2?logo=linkedin&logoColor=white)](https://linkedin.com/in/flávio-c)
[![GitHub](https://img.shields.io/badge/GitHub-FL4V10ARC-181717?logo=github&logoColor=white)](https://github.com/FL4V10ARC)
[![Email](https://img.shields.io/badge/Email-flaviocarvalho9029@gmail.com-D14836?logo=gmail&logoColor=white)](mailto:flaviocarvalho9029@gmail.com)
