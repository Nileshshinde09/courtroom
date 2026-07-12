# Courtroom Starter

Minimal Spring Boot base: PostgreSQL + Redis wired up, Flyway migrations, and a single
working endpoint — user registration. Everything else (login/JWT, cases, trials, etc.)
is left for you to add on top of this.

## Stack

- Java 21, Spring Boot 3.3.4
- PostgreSQL (via Spring Data JPA + Flyway)
- Redis (connection configured, not yet used for anything — ready for you to wire in sessions/caching later)
- Spring Security (only used here for BCrypt password hashing; all `/api/auth/**` routes are open)
- Bean Validation + a global exception handler

## Run it

1. Start Postgres + Redis:
   ```bash
   docker compose up -d
   ```

2. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```
   (or run `CourtroomApplication` directly from your IDE)

   Flyway will auto-run `V1__create_users_table.sql` against the `courtroom` database on startup.

3. Register a user:
   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "displayName": "Nilesh Patil",
       "username": "nilesh",
       "email": "nilesh@example.com",
       "password": "supersecret123"
     }'
   ```

   Success → `201 Created` with the created user (no password hash returned).
   Duplicate email/username → `409 Conflict`.
   Invalid input → `400 Bad Request` with field-level messages.

## Project layout

```
src/main/java/com/courtroom
├── CourtroomApplication.java
├── config/
│   └── SecurityConfig.java        # stateless, BCrypt bean, permits /api/auth/**
├── user/
│   ├── User.java                  # JPA entity
│   └── UserRepository.java
├── auth/
│   ├── RegisterRequest.java       # validated DTO
│   ├── UserResponse.java
│   ├── AuthService.java
│   └── AuthController.java        # POST /api/auth/register
└── exception/
    ├── ErrorResponse.java
    ├── GlobalExceptionHandler.java
    ├── EmailAlreadyExistsException.java
    └── UsernameAlreadyExistsException.java
```

## Notes for what you add next

- **Login/JWT**: add a `jwt` package (or extend `auth`) with token generation, add a filter
  in `SecurityConfig`, and change `.anyRequest().authenticated()` targets accordingly.
- **application.yml**: currently points at `localhost` for both Postgres and Redis with
  hardcoded dev credentials — fine for local dev, move to env vars before anything shared/prod.
- **ddl-auto is `validate`** on purpose — all schema changes should go through new Flyway
  migration files (`V2__...sql`, `V3__...sql`, etc.), never hand-edited.
