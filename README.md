
# Mini Bank System

A Spring Boot application for managing customers, accounts, and addresses in a mini bank system. This application supports CRUD operations for customers and provides functionalities for managing accounts and addresses.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [API Endpoints](#api-endpoints)
  - [Create Customer](#create-customer)
  - [Update Customer](#update-customer)
  - [Retrieve Customer](#retrieve-customer)
  - [Search Customers](#search-customers)

## Features

- **Customer Management**: Create, update, retrieve, and search for customers.
- **Address Management**: Handle addresses related to customers.
- **Global and Environment Variables**: Easily manage variable settings in Postman for testing.

## Technologies Used

- **Spring Boot**: Framework for building Java-based applications.
- **Hibernate**: ORM for database operations.
- **H2**: for storing data.
- **JUnit**: For unit testing.
- **Postman**: For API testing and documentation.

## Setup and Installation

### Prerequisites

- Java 11 or higher
- Maven
- H2 database 

### Clone the Repository

```bash
git clone https://github.com/yourusername/mini-bank-system.git
cd mini-bank-system
```

### Build and Run the Application

#### Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Running the Tests

```bash
mvn test
```

## API Endpoints

### Create Customer

- **URL**: `api/v1/customers`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "name": "John",
    "lastname": "Doe",
    "phoneNumber": "1234567890",
    "email": "john.doe@example.com",
    "customerType": "PRIVATE",
    "addresses": [
      {
        "street": "123 Elm Street",
        "city": "Somewhere",
        "state": "NY",
        "zipCode": "12345",
        "country": "USA"
      }
    ],
   "accountNumber": "ACCT12345"
  }
  ```

### Update Customer

- **URL**: `api/v1/customers/{id}`
- **Method**: `PUT`
- **Request Body**:
  ```json
  {
    "name": "John",
    "lastname": "Doe",
    "phoneNumber": "1234567890",
    "email": "john.doe@example.com",
    "customerType": "PRIVATE",
    "addresses": [
      {
        "street": "456 Oak Street",
        "city": "Somewhere",
        "state": "NY",
        "zipCode": "12345",
        "country": "USA"
      }
    ]
  }
  ```

### Retrieve Customer

- **URL**: `api/v1/customers/{id}`
- **Method**: `GET`

### Search Customers

- **URL**: `api/v1/customers/search?searchTerm=John&page=0&size=10`
- **Method**: `GET`
- **Query Parameters**:
  - `searchTerm`: The term to search for in names, lastnames or emails.
  - `page`: The page number for pagination.
  - `size`: The number of records per page.

