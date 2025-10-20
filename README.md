
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

**Request Payload Example 1 (with master date and per-product dates):**

```json
{
  "date": "2025-10-20",
  "products": [
    {
      "product_id": 1,
      "date": "2025-10-20",
      "colour": "Red",
      "unit": "kg",
      "weight": 12.5,
      "quantity": 100,
      "type": "Finished",
      "remark": "Morning batch"
    },
    {
      "product_id": 2,
      "date": "2025-10-19",
      "colour": "Blue",
      "unit": "pcs",
      "weight": 0,
      "quantity": 250,
      "type": "Semi-Finished",
      "remark": "Yesterday’s pending lot"
    }
  ],
  "remark": "Production for 20-Oct-2025"
}
```

**Request Payload Example 2 (without master date, uses today's date):**

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
  "status": "success",
  "message": "Daily production created",
  "data": [
    {
      "id": 5,
      "date": "2025-10-20",
      "masterRemark": "Production for 20-Oct-2025",
      "productId": 1,
      "productName": "PVC Pipe",
      "productDetailsId": 12,
      "type": "Finished",
      "colour": "Red",
      "unit": "kg",
      "weight": 12.5,
      "quantity": 100,
      "productRemark": "Morning batch"
    },
    {
      "id": 5,
      "date": "2025-10-19",
      "masterRemark": "Production for 20-Oct-2025",
      "productId": 2,
      "productName": "Plastic Sheet",
      "productDetailsId": 13,
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
            "id": 1,
            "date": "2025-10-20",
            "masterRemark": "Production for 20-Oct-2025",
            "productId": 4,
            "productName": "HDPE CRATE ACCESSORIES",
            "productDetailsId": 1,
            "type": "Finished",
            "colour": "Red",
            "unit": "kg",
            "weight": 12.5000,
            "quantity": 100.0000,
            "productRemark": "Morning batch"
        },
        {
            "id": 1,
            "date": "2025-10-19",
            "masterRemark": "Production for 20-Oct-2025",
            "productId": 5,
            "productName": "PVC Pipe",
            "productDetailsId": 2,
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

### Create Daily Stock

```json
{
  "date": "2025-09-15",
  "productIds": [1,3,4,5],
  "billNo": "BILL-2002",
  "remarks": "Added to packaging materials section"
}

```

### Get all Daily Stock List

```
localhost:8080/api/daily-stock
```
```json
{
    "success": true,
    "message": "Daily stock fetched",
    "data": [
        {
            "id": 1,
            "date": "2025-09-15",
            "billNo": "BILL-2001",
            "remarks": "Fresh stock received from vendor",
            "product": {
                "id": 1,
                "productName": "PP Bin Lid Clamp",
                "colour": "Natural",
                "type": "PP Bins Accessories",
                "unit": "Piece",
                "weight": "0.05 kg",
                "quantity": "1000"
            }
        },
        {
            "id": 2,
            "date": "2025-09-15",
            "billNo": "BILL-2002",
            "remarks": "Added to packaging materials section",
            "product": {
                "id": 4,
                "productName": "Stretch Film Roll 20 micron",
                "colour": "Transparent",
                "type": "Rolls",
                "unit": "Roll",
                "weight": "1.5 kg",
                "quantity": "300"
            }
        }
    ]
}

```

### Create Daily Sale

```json
{
  "date": "2025-09-15",
  "productIds":[1,3],
  "billNo": "SALE-3001",
  "remarks": "Sold 200 units to Client A"
}

```

### Get all Daily Sale List

```
localhost:8080/api/daily-sale
```
```json
{
    "success": true,
    "message": "Daily sale fetched",
    "data": [
        {
            "id": 1,
            "date": "2025-09-15",
            "billNo": "SALE-3001",
            "remarks": "Sold 200 units to Client A",
            "product": {
                "id": 2,
                "productName": "HDPE Crate Handle Injection Mold",
                "colour": "Black",
                "type": "HDPE Crate Accessories",
                "unit": "Pair",
                "weight": "0.12 kg",
                "quantity": "500"
            }
        },
        {
            "id": 2,
            "date": "2025-09-15",
            "billNo": "SALE-3002",
            "remarks": "Delivered to Project Site B",
            "product": {
                "id": 5,
                "productName": "PVC Wall Coving 50mm",
                "colour": "White",
                "type": "PVC Coving",
                "unit": "Meter",
                "weight": "0.4 kg",
                "quantity": "1000"
            }
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
