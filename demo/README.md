# E-commerce Order Review API

A REST API built with **Java 17 and Spring Boot** for an online store. Customers place orders, and can review a product **only if it was part of their order**. Each product in an order can be reviewed once, and the product's average rating updates automatically.

## Features

- Product catalog with **pagination**, and **search** by category, price range, keyword and minimum rating
- Order creation with the **price taken from the database** (never from the client), stock checking and stock reduction
- **Verified-purchase reviews**: a review must reference an order that contains the product, and duplicates are rejected
- Automatic product **average rating** and review count
- Request **validation** and a global **exception handler** with consistent JSON error responses
- Response **DTOs** (Java records), so entities are never exposed directly
- **Unit tests** with JUnit 5 and Mockito for the order and review rules

## Tech Stack

| Area | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5, Spring MVC |
| Persistence | Spring Data JPA, Hibernate, MySQL |
| Validation | Jakarta Bean Validation |
| Build | Maven |
| Testing | JUnit 5, Mockito |

## Project Structure

```
src/main/java/com/example/demo
├── controller    ProductController, OrderController, ProductReviewController
├── service       ProductService, OrderService
├── repository    ProductRepository, OrderRepository, ProductReviewRepository
├── entity        Product, ProductImage, ProductReview, Order, OrderItem
├── dto           request DTOs and response records
├── spec          ProductSpecification (dynamic search filters)
├── exception     ResourceNotFoundException, ErrorResponse, GlobalExceptionHandler
└── seed          ProductSeeder (demo data)
```

## Getting Started

### Prerequisites
- Java 17
- MySQL 8+
- Maven (or use the included `./mvnw` wrapper)

### Setup

1. Create the database:
   ```sql
   CREATE DATABASE ecommerce_store;
   ```
2. Set your credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_store
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Run the application:
   ```
   ./mvnw spring-boot:run
   ```
   The API starts on `http://localhost:8080`. On first run, `ProductSeeder` adds demo products to an empty table.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/products?page=0&size=5` | List products (paginated) |
| GET | `/api/products/{id}` | Get one product with its reviews |
| GET | `/api/products/search` | Search products (all parameters optional) |
| POST | `/api/orders` | Create an order |
| GET | `/api/orders/{orderNo}` | Get an order by order number |
| POST | `/api/products/reviews` | Add a review for a product in an order |

### Search parameters
`category`, `minPrice`, `maxPrice`, `keyword` (matches name or description), `ratings` (minimum rating).

Example: `GET /api/products/search?category=Phones&minPrice=500&ratings=4.5`

### Create an order

`POST /api/orders`
```json
{
  "orderitems": [
    { "productId": 1, "quantity": 2 }
  ]
}
```
Response `201 Created`:
```json
{ "orderNo": "2eae5ed9-35e8-400d-a94f-c4c7dee372d3" }
```
The total is calculated on the server from the product price stored in the database, plus a flat tax of 10. Any price sent by the client is ignored.

### Add a review

`POST /api/products/reviews`
```json
{
  "orderNo": "2eae5ed9-35e8-400d-a94f-c4c7dee372d3",
  "productId": 1,
  "comment": "Great phone",
  "rating": 4.5
}
```
Response `201 Created`. Rating must be between 1 and 5.

### Paginated response shape

```json
{
  "content": [ ],
  "page": 0,
  "size": 5,
  "totalElements": 11,
  "totalPages": 3
}
```

## Business Rules

- An order must contain at least one item, and each quantity must be greater than 0.
- An order is rejected if any product has insufficient stock, and nothing is saved in that case.
- Stock is reduced when an order is created.
- A review is accepted only if the order exists, the product exists, and the product is part of that order.
- Each product in an order can be reviewed only once. This is enforced in the service and by a unique constraint on `(order_id, product_id)`.
- Adding a review updates the product's average rating (rounded to one decimal) and review count in the same transaction.

## Error Responses

All errors return the same JSON structure:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Order not found: abc",
  "fieldErrors": null,
  "timestamp": "2026-10-10T14:51:11.505"
}
```

| Status | When |
|---|---|
| 400 | Validation failed (`fieldErrors` lists each invalid field) or the request body is malformed |
| 404 | Product, order or endpoint not found |
| 405 | Wrong HTTP method for the endpoint |
| 409 | Not enough stock, product not in the order, or duplicate review |
| 500 | Unexpected server error (details are logged, not returned) |

## Testing

Run all tests:
```
./mvnw test
```

The unit tests use JUnit 5 and Mockito, so they run without a database.

### OrderServiceTest

Each test proves something worth saying in an interview:

- **Test 1** shows that a customer can't change the price, which is the bug we fixed. The service uses the database price even when the client sends a different one, and it reduces stock.
- **Test 2** shows stock can't go negative, and that nothing is saved when the order fails.
- **Test 3** shows an unknown product gives a clean not-found error.

### ProductServiceTest

- **Test 1** shows a valid review is saved, linked to its order, and the product's average rating and review count update correctly.
- **Test 2** shows nobody can review a product that isn't part of the order.
- **Test 3** shows a second review for the same order and product is rejected, and the ratings don't change.
- **Test 4** shows an unknown order number gives a clean not-found error and nothing is saved.

## Design Decisions

- **Prices come from the database.** The client only sends product ids and quantities, so a customer can't pay a different price.
- **Constructor injection** in `OrderService`, which makes the class easy to unit test with mocks.
- **DTO records** for all responses, because returning entities directly can expose too much data and fail on circular references between products, reviews and orders.
- **`@Transactional`** on order creation and review creation, so stock, order, review and rating changes succeed or fail together.
- **Dynamic search** with Spring Data JPA `Specification`, so each filter is optional.

## Planned Improvements

- Spring Security with JWT authentication, and linking orders and reviews to users
- Product Review Platform split into microservices (auth, reviews, notifications) with an API Gateway
- `BigDecimal` for money and an enum for order status
- Integration tests with `@WebMvcTest` and `@DataJpaTest`
- Swagger/OpenAPI documentation
- Docker Compose setup

## Author

Monika Krishnamoorthy
