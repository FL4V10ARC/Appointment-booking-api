[![PT](https://img.shields.io/badge/🇧🇷-Português-2ea44f?style=for-the-badge)](./README.pt.md)
[![EN](https://img.shields.io/badge/🇺🇸-English-0A66C2?style=for-the-badge)](./README.md)

---

# 📅 Appointment Booking API

[![Java](https://img.shields.io/badge/Java%2021-ED8B00?logo=java&logoColor=white)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-000000?logo=jsonwebtokens&logoColor=white)](https://jwt.io)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?logo=postgresql&logoColor=white)](https://www.postgresql.org)
[![Flyway](https://img.shields.io/badge/Flyway-CC0200?logo=flyway&logoColor=white)](https://flywaydb.org)
[![JUnit5](https://img.shields.io/badge/JUnit%205-25A162?logo=junit5&logoColor=white)](https://junit.org/junit5)
[![Mockito](https://img.shields.io/badge/Mockito-6DB33F?logo=mockito&logoColor=white)](https://site.mockito.org/)
[![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black)](https://swagger.io)

> API REST para gerenciamento de agendamentos com autenticação JWT, controle de acesso por roles, regras de negócio reais e testes unitários com Mockito.

---

## Sobre o projeto

A **Appointment Booking API** simula um sistema real de agendamento de serviços. O projeto foi desenvolvido com foco em boas práticas de back-end: segurança com JWT, arquitetura em camadas, validação de dados, tratamento global de exceções e cobertura de testes.

---

## Fluxo da API

```
Client
  │
  ▼
[JwtAuthenticationFilter] → valida Bearer token em cada request
  │
  ▼
[SecurityConfig] → verifica ROLE (CLIENT / ADMIN)
  │
  ├── POST /auth/**              → público (register, login)
  ├── POST /appointments         → CLIENT only  → 201 Created
  ├── GET  /appointments/me      → CLIENT only  → 200 OK
  ├── GET  /appointments         → ADMIN only   → 200 OK
  └── DELETE /appointments/{id}  → CLIENT (próprio) | ADMIN → 204 No Content
  │
  ▼
Controller → Service → Repository → PostgreSQL
```

---

## Tecnologias

| Camada         | Tecnologia                     |
| -------------- | ------------------------------ |
| Linguagem      | Java 21                        |
| Framework      | Spring Boot 3                  |
| Segurança      | Spring Security + JWT          |
| Banco de dados | PostgreSQL                     |
| ORM            | JPA / Hibernate                |
| Migrations     | Flyway                         |
| Build          | Maven                          |
| Testes         | JUnit 5 + Mockito              |
| Documentação   | SpringDoc OpenAPI (Swagger UI) |

---

## Roles e Permissões

| Endpoint                  | CLIENT  | ADMIN |
| ------------------------- | :-----: | :---: |
| POST /auth/register       |   ✅    |  ✅   |
| POST /auth/login          |   ✅    |  ✅   |
| POST /appointments        |   ✅    |  ❌   |
| GET /appointments/me      |   ✅    |  ❌   |
| GET /appointments         |   ❌    |  ✅   |
| DELETE /appointments/{id} | próprio |  ✅   |

---

## Regras de negócio

- Não permite agendamento em data passada
- Não permite conflito de horários
- CLIENT só acessa e cancela seus próprios agendamentos
- ADMIN acessa e cancela qualquer agendamento

---

## Testes unitários

Cobertura com **JUnit 5 + Mockito** para os dois services principais:

**AppointmentService**

- Criação de agendamento com sucesso
- Rejeição de data no passado
- Rejeição de conflito de horário
- Cancelamento pelo dono (CLIENT)
- Rejeição de cancelamento por usuário sem permissão
- Cancelamento por ADMIN de agendamento de outro usuário

**UserService**

- Registro com sucesso
- Rejeição de email duplicado
- Login com sucesso
- Rejeição de senha inválida
- Rejeição de usuário inexistente

---

## Como rodar o projeto

### Pré-requisitos

- Java 21
- PostgreSQL rodando localmente
- Maven

### 1. Clone o repositório

```bash
git clone https://github.com/FL4V10ARC/appointment-booking-api.git
cd appointment-booking-api
```

### 2. Crie o banco de dados

```sql
CREATE DATABASE appointment_booking;
```

### 3. Configure o `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/appointment_booking
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
```

### 4. Rode a aplicação

```bash
./mvnw spring-boot:run
```

### 5. Rode os testes

```bash
./mvnw test
```

---

## Endpoints

### Autenticação

```bash
# Cadastro
POST /auth/register
{
  "name": "Flávio",
  "email": "flavio@email.com",
  "password": "123456"
}

# Login → retorna JWT
POST /auth/login
{
  "email": "flavio@email.com",
  "password": "123456"
}
```

### Agendamentos

```bash
# Criar agendamento → 201 Created
POST /appointments
Authorization: Bearer <token>
{ "appointmentTime": "2025-12-01T10:00:00" }

# Meus agendamentos → 200 OK
GET /appointments/me
Authorization: Bearer <token>

# Todos os agendamentos (ADMIN) → 200 OK
GET /appointments
Authorization: Bearer <token>

# Cancelar agendamento → 204 No Content
DELETE /appointments/{id}
Authorization: Bearer <token>
```

---

## Documentação interativa (Swagger)

![Swagger](./docs/swagger-home.jpeg)

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

Para testar endpoints autenticados:

1. Faça login em `POST /auth/login`
2. Copie o token retornado
3. Clique em **Authorize** no Swagger
4. Cole: `Bearer SEU_TOKEN`

---

## Autor

**Flávio Carvalho**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Flávio%20Carvalho-0A66C2?logo=linkedin&logoColor=white)](https://linkedin.com/in/flávio-c)
[![GitHub](https://img.shields.io/badge/GitHub-FL4V10ARC-181717?logo=github&logoColor=white)](https://github.com/FL4V10ARC)
[![Email](https://img.shields.io/badge/Email-flaviocarvalho9029@gmail.com-D14836?logo=gmail&logoColor=white)](mailto:flaviocarvalho9029@gmail.com)
