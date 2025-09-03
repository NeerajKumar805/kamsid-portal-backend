
# Kamsid Portal Backend

This is a Spring Boot backend for **Kamsid Portal**, built with Maven and PostgreSQL.  
It manages products and daily production entries with a clean layered architecture (Controller → Service → Repository).  
Standardized API responses and DTOs are used across endpoints.

---

## Tech Stack
- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Maven

---

## Project Structure
```

com.portal.kamsid
├── controller        # REST Controllers
├── dto               # DTOs for requests/responses
├── entity            # JPA entities (Product, DailyProductionMaster)
├── repository        # Spring Data JPA repositories
├── service           # Business logic layer
├── util              # Utility classes (API constants, ApiResponse, etc.)
└── exception         # Global exception handler

````
---

## API Endpoints

| Method | Endpoint                  | Description                      |
|--------|---------------------------|----------------------------------|
| POST   | `/api/products`           | Create a product                 |
| GET    | `/api/products`           | Get all products                 |
| GET    | `/api/products/{id}`      | Get a product by ID              |
| POST   | `/api/daily-production`   | Create a daily production record |
| GET    | `/api/daily-production`   | Get all daily productions        |

All responses are wrapped in a **standard `ApiResponse`**:
```json
{
  "success": true,
  "message": "Products fetched successfully",
  "data": [...]
}
````

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/NeerajKumar805/kamsid-portal-backend.git
cd kamsid-portal-backend
```

### 2. Configure PostgreSQL

Create a database in PostgreSQL (e.g. `kamsid_db`) and update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/kamsid_portal
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

The app will start at:
👉 `http://localhost:8080`

---

## Example Payloads

### Create Product

```json
{
  "productName": "PP BINS ACCESSORIES",
  "description": "Includes all the accessories required for PP bins fabrication.",
  "category": "Packaging",
  "weight": "250 g",
  "size": "15 MM"
}


```

### Get all Products

```
localhost:8080/api/products
```

```json
{
    "success": true,
    "message": "Products fetched",
    "data": [
        {
            "id": 1,
            "productName": "PALLET SLEEVE BOX",
            "description": "Includes all types of pallet sleeve boxes required for returnable packaging.",
            "category": "Packaging",
            "weight": "400 g",
            "size": "20 MM"
        },
        {
            "id": 2,
            "productName": "Dunnage & Dust covers",
            "description": "Includes all types of dunnage & dust covers.",
            "category": "Packaging",
            "weight": "200 g",
            "size": "14 MM"
        },
        {
            "id": 3,
            "productName": "PP BINS ACCESSORIES",
            "description": "Includes all the accessories required for PP bins fabrication.",
            "category": "Packaging",
            "weight": "250 g",
            "size": "15 MM"
        }
    ]
}


```

### Create Daily Production

```json
{
  "date": "2025-09-03",
  "product": { "id": 1 },
  "remarks": "Initial production entry for PP BINS ACCESSORIES"
}

```

### Get all Daily Production List

```
localhost:8080/api/daily-production
```
```json
{
    "success": true,
    "message": "Daily production fetched",
    "data": [
        {
            "id": 2,
            "date": "2025-09-03",
            "productId": 1,
            "productName": "PALLET SLEEVE BOX",
            "remarks": "Initial production entry for PP BINS ACCESSORIES"
        },
        {
            "id": 3,
            "date": "2025-09-04",
            "productId": 2,
            "productName": "Dunnage & Dust covers",
            "remarks": "Production started for KAMBO STORAGE SYSTEMS"
        },
        {
            "id": 4,
            "date": "2025-09-05",
            "productId": 3,
            "productName": "PP BINS ACCESSORIES",
            "remarks": "Trial batch completed for MODULAR OFFICE FURNITURE"
        }
    ]
}

```

---

## Notes

* API paths are not hardcoded — they are defined in a central constants class.
* Validation errors return consistent messages.
* DTOs are used for request/response instead of exposing entities.

---
