# Booking System (Backend)

Spring Boot backend for managing **ad spaces** (billboards, bus stops, mall displays, transit ads) and handling **booking requests** with an approval workflow.

---

## What this backend does

### Ad Spaces

- Lists available ad spaces (optionally filtered by type and city)
- Fetches ad space details by ID
- creates new ad spaces via POST

### Booking Requests

- Creates booking requests for an ad space over a date range
- Prevents overlapping bookings when an **APPROVED** booking already exists
- Supports approval/rejection of PENDING requests
- Calculates total cost from `(days * pricePerDay)`

---

## Tech Stack

- Java 17
- Spring Boot 3.x (Web, Validation, Data JPA)
- PostgreSQL
- Flyway migrations
- Hibernate
- OpenAPI/Swagger (springdoc)

---

## API (Quick Overview)

Base path: `/api/v1`

### Ad Spaces

- `GET /ad-spaces?type=BILLBOARD&city=NYC`
- `GET /ad-spaces/{id}`
- `POST /ad-spaces`

### Booking Requests

- `POST /booking-requests`
- `GET /booking-requests?status=PENDING`
- `GET /booking-requests/{id}`
- `PATCH /booking-requests/{id}/approve`
- `PATCH /booking-requests/{id}/reject`

Swagger UI link:

- `http://localhost:8080/swagger-ui/index.html`

---

## Running with Docker

This project ships with a `docker-compose.yml` that starts:

- **PostgreSQL** (`db`) on `localhost:5432`
- **Spring Boot API** (`app`) on `localhost:8080`

### Prerequisites

- Docker Desktop (Windows/macOS) or Docker Engine (Linux)
- Docker Compose (usually included with Docker Desktop)

---

### 1) Start the stack

From the project root (where `docker-compose.yml` is):

```bash
docker compose up --build
```

What happens:

- The `db` container starts first
- `db` becomes “healthy” using `pg_isready`
- The `app` container starts after `db` is healthy
- Spring Boot connects to Postgres via:
  `jdbc:postgresql://db:5432/ad_space_booking_system`

---

### 2) Verify it’s running

- API: `http://localhost:8080`
- Swagger UI link: `http://localhost:8080/swagger-ui/index.html`

Quick checks:

```bash
curl http://localhost:8080/api/v1/ad-spaces
curl http://localhost:8080/api/v1/booking-requests
```

---

### 3) Stop containers

```bash
docker compose down
```

---

### 4) Reset the database (wipe volumes)

If you want a fresh database (remove persisted Postgres data):

```bash
docker compose down -v --remove-orphans
```

---

## Project Structure (typical)

- `com.generatik.challenge.adspace`
  - `api/` controllers + mappers + DTOs
  - `service/` business logic
  - `repo/` Spring Data repositories
  - `model/` JPA entities + enums
- `com.generatik.challenge.booking`
  - `api/` controllers + mappers + DTOs
  - `service/` booking logic (overlap checks, pricing)
  - `repo/` repositories
  - `model/` entities + enums

---

## License

Coding challenge project.
