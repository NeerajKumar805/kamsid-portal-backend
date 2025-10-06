
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

### Create Product

```json
{
  "productName": "Heavy-Duty Dust Cover Sheet 6×6 ft",
  "type": "Dunnage & Dust Covers",
  "colour": "Gray",
  "unit": "Piece",
  "weight": "1.8 kg",
  "quantity": "150"
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
            "productName": "PP Bin Lid Clamp",
            "colour": "Natural",
            "type": "PP Bins Accessories",
            "unit": "Piece",
            "weight": "0.05 kg",
            "quantity": "1000"
        },
        {
            "id": 2,
            "productName": "HDPE Crate Handle Injection Mold",
            "colour": "Black",
            "type": "HDPE Crate Accessories",
            "unit": "Pair",
            "weight": "0.12 kg",
            "quantity": "500"
        },
        ... (Many more products)
    ]
}


```

### Create Daily Production

```json
{
  "date": "2025-09-15",
  "productIds": [1,2,3],
  "remarks": "New batch completed and moved to storage"
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
            "id": 1,
            "date": "2025-09-15",
            "remarks": "Produced 150 units in Plant A",
            "product": {
                "id": 3,
                "productName": "Pallet Sleeve Box – 1000×800 mm",
                "colour": "Blue",
                "type": "Pallet Sleeve Box",
                "unit": "Box",
                "weight": "2.5 kg",
                "quantity": "200"
            }
        },
        {
            "id": 2,
            "date": "2025-09-15",
            "remarks": "New batch completed and moved to storage",
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
