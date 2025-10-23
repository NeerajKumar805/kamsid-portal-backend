# Kamsid Portal Backend

Production and sale automatically update inventory. Use these endpoints and DTO shapes directly.

---

## Tech

* Java 21+, Spring Boot 3.x, Spring Data JPA, PostgreSQL, Maven

---

## Key behavior (must-know)

* Inventory is the source of truth (`ProductInventory`).
* Every change writes an audit `StockEntry`.

  * Production → inventory IN
  * Sale → inventory OUT
  * Manual stock (purchase) → inventory IN
* Stock update runs inside the same transaction as the master save. If stock update fails (e.g., insufficient stock), the whole request rolls back.
* Global history endpoint: `GET /api/inventory/history`
* Product-specific history: `GET /api/inventory/{productId}/entries`

---

## API summary (important endpoints)

| Method |                             Endpoint | Purpose                                              |
| ------ | -----------------------------------: | ---------------------------------------------------- |
| POST   |                      `/api/products` | Create product                                       |
| GET    |                      `/api/products` | List products                                        |
| POST   |              `/api/daily-production` | Create production (updates inventory IN)             |
| GET    |              `/api/daily-production` | List/Query production                                |
| POST   |                    `/api/daily-sale` | Create sale (updates inventory OUT)                  |
| GET    |                    `/api/daily-sale` | List/Query sales                                     |
| POST   |                   `/api/daily-stock` | Manual stock/purchase (updates inventory IN)         |
| GET    |                   `/api/daily-stock` | List/Query manual stock                              |
| GET    |                     `/api/inventory` | List current inventory                               |
| GET    |         `/api/inventory/{productId}` | Inventory for product                                |
| GET    |             `/api/inventory/history` | Global stock history (filters)                       |
| GET    | `/api/inventory/{productId}/entries` | History for product                                  |
| POST   |       `/api/inventory/admin/rebuild` | Admin: rebuild inventory from history (protect this) |

All responses use `ApiResponse<T>`:

```json
{ "success": true|false, "message": "...", "data": <T|null> }
```

---

## Example payloads (short)

### Create Product

`POST /api/products`

```json
{ "productName": "PVC Pipe" }
```

### Create Production

`POST /api/daily-production`

```json
{
  "products": [
    { "product_id": 1, "unit": "kg", "quantity": 75, "type": "Finished", "remark": "Afternoon run" }
  ],
  "remark": "today's run"
}
```

### Create Sale

`POST /api/daily-sale`

```json
{
  "products": [
    { "product_id": 1, "unit": "pcs", "quantity": 5, "type": "Retail" }
  ],
  "remark": "local sale",
  "billNo": "SALE-2025-001"
}
```

### Create Manual Stock (purchase)

`POST /api/daily-stock`

```json
{
  "products": [
    { "product_id": 1, "unit": "kg", "quantity": 200, "type": "Opening Stock" }
  ],
  "remark": "supplier delivery",
  "billNo": "STOCK-2025-010"
}
```

---

## Important responses (examples)

### Success (inventory fetch)

`GET /api/inventory/1`

```json
{
  "success": true,
  "message": "Inventory fetched",
  "data": { "productId": 1, "productName": "PVC Pipe", "quantity": 97.5, "lastUpdated": "2025-10-23" }
}
```

### Global history

`GET /api/inventory/history?start=2025-10-01&end=2025-10-23`

```json
{
  "success": true,
  "message": "Stock entries fetched",
  "data": [
    { "id": 101, "productId": 1, "quantity": 50, "direction": "IN", "sourceModule": "PRODUCTION", "entryDate": "2025-10-22" },
    { "id": 87,  "productId": 1, "quantity": 2,  "direction": "OUT", "sourceModule": "SALE", "entryDate": "2025-10-22" }
  ]
}
```

### Insufficient stock (sale rejected)

HTTP 409

```json
{
  "success": false,
  "message": "Insufficient stock for product 1 (PVC Pipe). Available: 2, required: 5",
  "data": null
}
```

---

## DTOs (what frontend expects)

`InventoryDto`

```json
{ "productId": 1, "productName": "PVC Pipe", "quantity": 125.5, "lastUpdated": "2025-10-23" }
```

`StockEntryDto`

```json
{
  "id": 101,
  "productId": 1,
  "productName": "PVC Pipe",
  "quantity": 75,
  "direction": "IN",
  "sourceModule": "PRODUCTION",
  "referenceMasterId": 2,
  "referenceProductDetailsId": 3,
  "entryDate": "2025-10-22",
  "remark": "Afternoon run"
}
```

`DailyMasterResponseDto`

* Fields: `id, date, masterRemark, moduleType (PRODUCTION|SALE|STOCK), productId, productName, productDetailsId, type, colour, unit, weight, quantity, productRemark, masterBillNo` (when applicable)

---

## DB notes (only what frontend needs)

* Inventory rows: `product_inventory` (product_id unique, quantity numeric)
* Audit: `stock_entry` (direction IN/OUT, sourceModule, reference_master_id, reference_product_details_id)
* Global history endpoint reads `stock_entry` with filters for productId/start/end/module/direction.

---

## Rollout tip (short)

1. Deploy with `stock.allow-negative: true` to avoid blocking live sales while validating.
2. Backfill inventory via `POST /api/inventory/admin/rebuild` (admin-only).
3. Switch `stock.allow-negative` to `false` when verified.

---
