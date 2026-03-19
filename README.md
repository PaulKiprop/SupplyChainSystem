# SupplyChainSystem

Spring Boot backend for a supply chain management system. The API manages products, suppliers, warehouses, inventory, analytics, and orders from creation to delivery.

## Tech Stack

- Java 25
- Spring Boot 4
- Spring Data JPA
- MySQL 8.4
- Swagger UI
- Maven
- Docker Compose

## What A Reviewer Needs

- Java 25 installed
- Docker Desktop running
- Internet access for Maven dependencies on first build

## Run The Project

1. Start MySQL:

```powershell
docker compose up -d
```

2. Run the application with the development profile:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

The `dev` profile will:

- create a fresh schema
- load deterministic sample data

## Access The API

- Application: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Run Tests

```powershell
.\mvnw.cmd test
```

## Notes

- Local MySQL runs from `compose.yaml`
- Development settings are in `src/main/resources/application-dev.yaml`
- The seed loader only runs under the `dev` profile
