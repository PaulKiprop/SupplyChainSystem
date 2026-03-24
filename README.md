# Supply Chain Management System

Spring Boot backend for a supply chain management system. 
The project exposes REST APIs for managing products, suppliers, warehouses, inventory, purchase orders, and stock analytics. 
It is designed as a backend-only application, with Swagger UI enabled to make manual testing and API exploration straightforward during development.

The application supports two practical local run modes:

- the default profile for normal development against a local MySQL database
- the `dev` profile for quick testing with a recreated schema and deterministic sample data loaded at startup

## Features

- Product CRUD operations
- Supplier management and product assignment
- Warehouse CRUD operations
- Inventory creation, lookup, and quantity adjustment
- Order creation and order status tracking
- Stock analytics endpoints
- Pageable API responses for list endpoints
- Swagger UI for interactive API testing

## Tech Stack

- Java 25
- Spring Boot 4
- Spring Data JPA
- MySQL 8.4
- Springdoc / Swagger UI
- Maven
- Docker Compose

## Prerequisites

- Java 25
- Maven wrapper support via `mvnw` / `mvnw.cmd`
- MySQL running locally on port `3306`

You can provide MySQL in either of these ways:

- install MySQL locally and create the database yourself
- run MySQL through Docker Compose

## Database Setup

The application expects a MySQL database named `supply_chain_db`.


## Run The Project

### Option 1: Use local MySQL

Make sure MySQL is already installed, running, and that the `supply_chain_db` database exists.

Run the application with the default profile in IntelliJ or :

```powershell
.\mvnw.cmd spring-boot:run
```

### Option 2: Use Docker Compose for MySQL

Start MySQL:

```powershell
docker compose up -d
```

Then run the application with either profile as needed.

## Profiles

### Default Profile

Runs with `src/main/resources/application.yaml`.

```powershell
.\mvnw.cmd spring-boot:run
```

Use this profile when you want to work against a clean local MySQL setup without reseeding the database on every launch.

### Dev Profile

Runs with `src/main/resources/application-dev.yaml`.

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

The `dev` profile will:

- recreate the schema on startup with `ddl-auto: create`
- load deterministic sample data for easier manual testing
- seed products, suppliers, warehouses, and inventory records

Use the dev Profile for faster testing for the Rest Api endpoints. 
## Access The API

- Application base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Run Tests

```powershell
.\mvnw.cmd test
```

## Notes

- The MySQL Docker service is defined in `compose.yaml`
- The seed loader only runs under the `dev` profile
- The `dev` profile is intended for demo and manual API testing, not for preserving existing local data
