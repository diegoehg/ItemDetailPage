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

## Design Choices

### Persistence
I opted for having 3 entities: Product, Seller and Payment Methods. Products are the main entity
that corresponds to an Item data: price, name, description, etc. Seller entity corresponds to
the store data, although in this app it only has a name. Payment Method is related with the
different methods available to pay for an item, like cash, credit card, some service like PayPal,
etc.

There is a one-to-many relationship between a Product and a Seller; a product is only
sold by one seller, but a seller can sell many different products. This is necessary if
I want to add a store view from where to buy different products from the same seller.
There is a many-to-many relationship between Sellers and Payment Methods. In case I
want to implement common logic related with a payment method, this can be reused across
sellers that accept that payment method.

There are 4 tables that persist this data: `products`, `sellers`, `products_images`,
`payment_methods` and `seller_payment_methods`. The `seller_payment_methods` is a pivot
table that associates different Sellers with different Payment Methods.
`products_images` contains URLs of a product's pictures. I feel that it is more
flexible to save the images' URLs in its own table.

### Service Layer
In this application, this layer has two purposes: converting entities into DTOs, and
validating if an ID corresponds to an existing element.

Given that there is a relationship between Product and Seller entities, one way to
avoid infinite recursion when parsing the entity data to JSON is to use DTO, to select
which information to pass through the API. Besides that, it decouples an entity
structure from a response structure. I put the entity-to-DTO conversion logic inside
a mapper. This mapper is invoked from the service layer.

In the product service layer I added a validation for checking if an ID corresponds to
an existing product. I case there is no product a custom exception is thrown. The
purpose is to unify the logic that returns a 404 error when invoking handlers that
require to get, modify or delete an specific element. A GlobalException handler is used
for this purpose.

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
