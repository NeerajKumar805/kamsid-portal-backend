
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

**Endpoint:**
`POST /api/daily-production`

**Request Payload Example:**

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
      "date": "2025-10-22",
      "masterRemark": "Production for 22-Oct-2025",
      "moduleType": "PRODUCTION",
      "productId": 1,
      "productName": "PVC Pipe",
      "productDetailsId": 3,
      "type": "Finished",
      "colour": "White",
      "unit": "kg",
      "weight": 8.5,
      "quantity": 75,
      "productRemark": "Afternoon run",
      "billNo": null
    },
    {
      "id": 2,
      "date": "2025-10-22",
      "masterRemark": "Production for 22-Oct-2025",
      "moduleType": "PRODUCTION",
      "productId": 3,
      "productName": "Green Cap",
      "productDetailsId": 4,
      "type": "Finished",
      "colour": "Green",
      "unit": "pcs",
      "weight": 0,
      "quantity": 120,
      "productRemark": "Special order",
      "billNo": null
    }
  ]
}
```

### 2.2 Get Daily Production by Date Range

**Endpoint:**
`GET /api/daily-production?start=2025-10-20&end=2025-10-22`

**Response Payload:**

```json
{
  "success": true,
  "message": "Daily production fetched",
  "data": [
    {
      "id": 2,
      "date": "2025-10-22",
      "masterRemark": "Production for 22-Oct-2025",
      "moduleType": "PRODUCTION",
      "productId": 1,
      "productName": "PVC Pipe",
      "productDetailsId": 3,
      "type": "Finished",
      "colour": "White",
      "unit": "kg",
      "weight": 8.5,
      "quantity": 75,
      "productRemark": "Afternoon run",
      "billNo": null
    },
    {
      "id": 1,
      "date": "2025-10-21",
      "masterRemark": "Production for 21-Oct-2025",
      "moduleType": "PRODUCTION",
      "productId": 2,
      "productName": "HDPE Crate",
      "productDetailsId": 2,
      "type": "Semi-Finished",
      "colour": "Blue",
      "unit": "pcs",
      "weight": 0,
      "quantity": 200,
      "productRemark": "Evening batch",
      "billNo": null
    }
  ]
}
```

---

## 3. Daily Sale API

### 3.1 Create Daily Sale

**Endpoint:**
`POST /api/daily-sale`

**Request Payload Example:**

```json
{
  "products": [
    {
      "product_id": 5,
      "colour": "Black",
      "unit": "pcs",
      "weight": 0,
      "quantity": 50,
      "type": "Retail",
      "remark": "Local shop delivery",
      "billNo": "SALE-2025-120"
    },
    {
      "product_id": 2,
      "colour": "Blue",
      "unit": "kg",
      "weight": 25.5,
      "quantity": 30,
      "type": "Wholesale",
      "remark": "Bulk order",
      "billNo": "SALE-2025-121"
    }
  ],
  "remark": "Sales completed"
}
```

**Response Payload:**

```json
{
  "success": true,
  "message": "Daily sale created",
  "data": [
    {
      "id": 7,
      "date": "2025-10-22",
      "masterRemark": "Sales entry for 22-Oct-2025",
      "moduleType": "SALE",
      "productId": 5,
      "productName": "Plastic Bottle",
      "productDetailsId": 9,
      "type": "Retail",
      "colour": "Black",
      "unit": "pcs",
      "weight": 0,
      "quantity": 50,
      "productRemark": "Local shop delivery",
      "billNo": "SALE-2025-120"
    },
    {
      "id": 7,
      "date": "2025-10-22",
      "masterRemark": "Sales entry for 22-Oct-2025",
      "moduleType": "SALE",
      "productId": 2,
      "productName": "PVC Rod",
      "productDetailsId": 10,
      "type": "Wholesale",
      "colour": "Blue",
      "unit": "kg",
      "weight": 25.5,
      "quantity": 30,
      "productRemark": "Bulk order",
      "billNo": "SALE-2025-121"
    }
  ]
}
```

### 3.2 Get Daily Sale by Date Range

**Endpoint:**
`GET /api/daily-sale?start=2025-10-20&end=2025-10-22`

**Response Payload:**

```json
{
  "success": true,
  "message": "Daily sale fetched",
  "data": [
    {
      "id": 7,
      "date": "2025-10-22",
      "masterRemark": "Sales entry for 22-Oct-2025",
      "moduleType": "SALE",
      "productId": 5,
      "productName": "Plastic Bottle",
      "productDetailsId": 9,
      "type": "Retail",
      "colour": "Black",
      "unit": "pcs",
      "weight": 0,
      "quantity": 50,
      "productRemark": "Local shop delivery",
      "billNo": "SALE-2025-120"
    }
  ]
}
```

---

## 4. Daily Stock API

### 4.1 Create Daily Stock

**Endpoint:**
`POST /api/daily-stock`

**Request Payload Example:**

```json
{
  "products": [
    {
      "product_id": 1,
      "colour": "White",
      "unit": "kg",
      "weight": 12.5,
      "quantity": 200,
      "type": "Opening Stock",
      "remark": "Morning count",
      "billNo": "STOCK-2025-010"
    },
    {
      "product_id": 3,
      "colour": "Green",
      "unit": "pcs",
      "weight": 0,
      "quantity": 500,
      "type": "Closing Stock",
      "remark": "End of day",
      "billNo": "STOCK-2025-011"
    }
  ],
  "remark": "Stock update for today"
}
```

**Response Payload:**

```json
{
  "success": true,
  "message": "Daily stock created",
  "data": [
    {
      "id": 11,
      "date": "2025-10-22",
      "masterRemark": "Stock entry for 22-Oct-2025",
      "moduleType": "STOCK",
      "productId": 1,
      "productName": "PVC Pipe",
      "productDetailsId": 12,
      "type": "Opening Stock",
      "colour": "White",
      "unit": "kg",
      "weight": 12.5,
      "quantity": 200,
      "productRemark": "Morning count",
      "billNo": "STOCK-2025-010"
    },
    {
      "id": 11,
      "date": "2025-10-22",
      "masterRemark": "Stock entry for 22-Oct-2025",
      "moduleType": "STOCK",
      "productId": 3,
      "productName": "Green Cap",
      "productDetailsId": 13,
      "type": "Closing Stock",
      "colour": "Green",
      "unit": "pcs",
      "weight": 0,
      "quantity": 500,
      "productRemark": "End of day",
      "billNo": "STOCK-2025-011"
    }
  ]
}
```

### 4.2 Get Daily Stock by Date Range

**Endpoint:**
`GET /api/daily-stock?start=2025-10-20&end=2025-10-22`

**Response Payload:**

```json
{
  "success": true,
  "message": "Daily stock fetched",
  "data": [
    {
      "id": 11,
      "date": "2025-10-22",
      "masterRemark": "Stock entry for 22-Oct-2025",
      "moduleType": "STOCK",
      "productId": 1,
      "productName": "PVC Pipe",
      "productDetailsId": 12,
      "type": "Opening Stock",
      "colour": "White",
      "unit": "kg",
      "weight": 12.5,
      "quantity": 200,
      "productRemark": "Morning count",
      "billNo": "STOCK-2025-010"
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
