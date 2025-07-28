# ItemDetailPage

This is an application inspired in an e-commerce site. Up to now, the application shows
a products list, that is prepopulated at running the application.

## Application Structure

The stack I used for implementing this application is the following:
- React at the front
- Node version 18
- Spring Boot at the back
- Java 21
- PostgreSQL 15 as a database

I used this stack because I'm familiarized with it, and it is a quite common stack. Given that
I was using an AI agent, I wanted to use well-known tools.

Both parts of this application, frontend and backend are dockerized; each part has its
corresponding `Dockerfile`. Each `Dockerfile` is divided into two stages, a build stage
and a final stage. I added a `docker-compose.yml` file at the root, for running the
whole application with Docker Compose. I feel that this is a practical way to build the
app and run the app.

## Backend API Description

This section describes the API endpoints available in the backend.

### GET /api/products

Retrieves a paginated list of products.

#### Query Parameters
- `page`: Page number (default: 1)
- `size`: Number of items per page (default: 10)

#### Response Body
```json
{
  "status": "SUCCESS",
  "code": 200,
  "data": {
    "content": [
      {
        "id": 1,
        "title": "Product Title",
        "description": "Product Description",
        "images": ["image_url1", "image_url2"],
        "price": 99.99,
        "seller": {
          "id": 1,
          "name": "Seller Name",
          "paymentMethods": [
            {
              "id": 1,
              "name": "Payment Method"
            }
          ]
        }
      }
    ],
    "totalPages": 5,
    "totalElements": 50,
    "size": 10,
    "number": 0
  }
}
```

### GET /api/products/{id}

Retrieves a specific product by ID.

#### Response Body
```json
{
  "status": "SUCCESS",
  "code": 200,
  "data": {
    "id": 1,
    "title": "Product Title",
    "description": "Product Description",
    "images": ["image_url1", "image_url2"],
    "price": 99.99,
    "seller": {
      "id": 1,
      "name": "Seller Name",
      "paymentMethods": [
        {
          "id": 1,
          "name": "Payment Method"
        }
      ]
    }
  }
}
```

### POST /api/products

Creates a new product.

#### Request Structure
```json
{
  "title": "Product Title",
  "description": "Product Description",
  "images": ["image_url1", "image_url2"],
  "price": 99.99,
  "seller": {
    "id": 1
  }
}
```

#### Response Body
```json
{
  "status": "SUCCESS",
  "code": 201,
  "data": {
    "id": 1,
    "title": "Product Title",
    "description": "Product Description",
    "images": ["image_url1", "image_url2"],
    "price": 99.99,
    "seller": {
      "id": 1,
      "name": "Seller Name",
      "paymentMethods": [
        {
          "id": 1,
          "name": "Payment Method"
        }
      ]
    }
  }
}
```

### PUT /api/products/{id}

Updates an existing product.

#### Request Structure
```json
{
  "title": "Updated Product Title",
  "description": "Updated Product Description",
  "images": ["image_url1", "image_url2"],
  "price": 149.99,
  "seller": {
    "id": 1
  }
}
```

#### Response Body
```json
{
  "status": "SUCCESS",
  "code": 200,
  "data": {
    "id": 1,
    "title": "Updated Product Title",
    "description": "Updated Product Description",
    "images": ["image_url1", "image_url2"],
    "price": 149.99,
    "seller": {
      "id": 1,
      "name": "Seller Name",
      "paymentMethods": [
        {
          "id": 1,
          "name": "Payment Method"
        }
      ]
    }
  }
}
```

### DELETE /api/products/{id}

Deletes a product.

#### Response Body
```json
{
  "status": "SUCCESS",
  "code": 204,
  "data": null
}
```

### GET /api/sellers

Retrieves a list of all sellers.

#### Response Body
```json
{
  "status": "SUCCESS",
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "Seller Name",
      "paymentMethods": [
        {
          "id": 1,
          "name": "Payment Method"
        }
      ]
    }
  ]
}
```

### Errors

The API may return the following error responses:

#### 400 Bad Request
Returned when the request contains invalid data.

```json
{
  "status": "ERROR",
  "code": 400,
  "message": "Validation failed: title - Title is required, price - Price must be greater than zero"
}
```

#### 404 Not Found
Returned when the requested resource does not exist.

```json
{
  "status": "ERROR",
  "code": 404,
  "message": "Product with id 999 not found"
}
```

#### 500 Internal Server Error
Returned when an unexpected error occurs on the server.

```json
{
  "status": "ERROR",
  "code": 500,
  "message": "An unexpected error occurred: Error message"
}
```
