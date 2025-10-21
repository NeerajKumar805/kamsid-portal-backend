
# Kamsid Portal Backend

This is a Spring Boot backend for **Kamsid Portal**, built with Maven and PostgreSQL.  
It manages products and daily production entries with a clean layered architecture.  
Standardized API responses and DTOs are used across endpoints.

---

## Tech Stack
- Java 21+
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
├── entity            # JPA entities
├── repository        # Spring Data JPA repositories
├── service           # Business logic layer
├── util              # Utility classes
└── exception         # Global exception handler

````
---

## API Endpoints Examples

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

Create a database in PostgreSQL (`kamsid_portal`) and update `src/main/resources/application.properties`:

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

## 1. Product API

### 1.1 Create Product

**Endpoint:** `POST /api/products`

**Request Payload:**

```json
{
  "productName": "PVC Pipe"
}
```

**Response Payload:**

```json
{
  "status": "success",
  "message": "Product created",
  "data": {
    "id": 1,
    "productName": "PVC Pipe"
  }
}
```

### 1.2 Get All Products

**Endpoint:** `GET /api/products`

**Response Payload:**

```json
{
  "status": "success",
  "message": "Products fetched",
  "data": [
    {
      "id": 1,
      "productName": "PVC Pipe"
    },
    {
      "id": 2,
      "productName": "Plastic Sheet"
    }
  ]
}
```

### 1.3 Get Product by ID

**Endpoint:** `GET /api/products/{id}`

**Response Payload:**

```json
{
  "status": "success",
  "message": "Product fetched",
  "data": {
    "id": 1,
    "productName": "PVC Pipe"
  }
}
```

---

## 2. Daily Production API

### 2.1 Create Daily Production

**Endpoint:** `POST /api/daily-production`

**Request Payload Example 2: **

```json
{
  "products": [
    {
      "product_id": 1,
      "colour": "White",
      "unit": "kg",
      "weight": 8.5,
      "quantity": 75,
      "type": "Finished",
      "remark": "Afternoon run"
    },
    {
      "product_id": 3,
      "colour": "Green",
      "unit": "pcs",
      "weight": 0,
      "quantity": 120,
      "type": "Finished",
      "remark": "Special order"
    }
  ],
  "remark": "Auto-date master (today’s entry)"
}
```

**Response Payload:**

```json
{
    "success": true,
    "message": "Daily production created",
    "data": [
        {
            "id": 2,
            "date": "2025-10-20",
            "masterRemark": "Production for 20-Oct-2025",
            "moduleType": "PRODUCTION",
            "productId": 1,
            "productName": "PVC Pipe",
            "productDetailsId": 3,
            "type": "Finished",
            "colour": "Red",
            "unit": "kg",
            "weight": 12.5,
            "quantity": 100,
            "productRemark": "Morning batch"
        },
        {
            "id": 2,
            "date": "2025-10-19",
            "masterRemark": "Production for 20-Oct-2025",
            "moduleType": "PRODUCTION",
            "productId": 2,
            "productName": "HDPE CRATE ACCESSORIES",
            "productDetailsId": 4,
            "type": "Semi-Finished",
            "colour": "Blue",
            "unit": "pcs",
            "weight": 0,
            "quantity": 250,
            "productRemark": "Yesterday’s pending lot"
        }
    ]
}
```

### 2.2 Get Daily Production

**Endpoint:** `GET /api/daily-production?start=yyyy-MM-dd&end=yyyy-MM-dd`

```
{
    "success": true,
    "message": "Daily production fetched",
    "data": [
        {
            "id": 2,
            "date": "2025-10-20",
            "masterRemark": "Production for 20-Oct-2025",
            "moduleType": "PRODUCTION",
            "productId": 1,
            "productName": "PVC Pipe",
            "productDetailsId": 3,
            "type": "Finished",
            "colour": "Red",
            "unit": "kg",
            "weight": 12.5000,
            "quantity": 100.0000,
            "productRemark": "Morning batch"
        },
        {
            "id": 2,
            "date": "2025-10-19",
            "masterRemark": "Production for 20-Oct-2025",
            "moduleType": "PRODUCTION",
            "productId": 2,
            "productName": "HDPE CRATE ACCESSORIES",
            "productDetailsId": 4,
            "type": "Semi-Finished",
            "colour": "Blue",
            "unit": "pcs",
            "weight": 0.0000,
            "quantity": 250.0000,
            "productRemark": "Yesterday’s pending lot"
        }
    ]
}

```
  * Stock and sales will be the same as daily production.

---

## Notes

* API paths are not hardcoded — they are defined in a central constants class.
* Validation errors return consistent messages.
* DTOs are used for request/response instead of exposing entities.

---
