# Smart Campus API

**Module:** 5COSC022W – Client-Server Architectures  
**Coursework:** Smart Campus Sensor & Room Management API  
**Student Name:** H.A.K Senadhi Mandina  
**Student ID:** w2120613
**Email:** w2120613@westminster.ac.uk

---

## 1. API Overview

This project implements a RESTful Smart Campus API using **JAX-RS**. The API manages three core resources:

- **Room**
- **Sensor**
- **SensorReading**

The API supports room creation, room lookup, room deletion with safety checks, sensor registration, sensor filtering, nested sensor readings, custom exception mapping and request/response logging.

The system uses **in-memory data structures only** (`HashMap` and `ArrayList`) and does **not** use any database technology. Therefore, all stored data is reset whenever the server is restarted or redeployed. The coursework explicitly requires JAX-RS only and forbids using Spring Boot or any database. :contentReference[oaicite:1]{index=1}

---

## 2. Technology Stack

- Java
- Maven
- JAX-RS (Jersey)
- Apache Tomcat
- NetBeans
- Postman

---

## 3. How to Build and Run the Project

### Run in NetBeans
1. Open the project in NetBeans.
2. Make sure Apache Tomcat is configured in NetBeans.
3. Right-click the project and select **Clean and Build**.
4. Right-click the project and select **Run**.
5. Open the API using a browser or Postman.

### Build with Maven
From the project folder:

```bash
https://github.com/SenadhiMandina/smart-campus-api
````

Then deploy the generated WAR file to Tomcat.

### Base URL

```text
http://localhost:8080/SmartCampusAPI/api/v1
```

The coursework requires the API entry point to use `@ApplicationPath("/api/v1")`. 

---

## 4. API Endpoints

### Discovery

* `GET /api/v1`

### Rooms

* `GET /api/v1/rooms`
* `POST /api/v1/rooms`
* `GET /api/v1/rooms/{roomId}`
* `DELETE /api/v1/rooms/{roomId}`

### Sensors

* `GET /api/v1/sensors`
* `GET /api/v1/sensors?type=Temperature`
* `GET /api/v1/sensors/{sensorId}`
* `POST /api/v1/sensors`

### Sensor Readings

* `GET /api/v1/sensors/{sensorId}/readings`
* `POST /api/v1/sensors/{sensorId}/readings`

These match the coursework tasks for discovery, room management, sensor operations, filtered retrieval and sub-resources for readings. 

---

## 5. Sample curl Commands

The brief requires at least five sample `curl` commands in the GitHub repository README. 

### 1. Discovery endpoint

```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1"
```

### 2. Create a room

```bash
curl -X POST "http://localhost:8080/SmartCampusAPI/api/v1/rooms" \
-H "Content-Type: application/json" \
-d '{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 40
}'
```

### 3. Get all rooms

```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/rooms"
```

### 4. Get a room by ID

```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301"
```

### 5. Create a sensor

```bash
curl -X POST "http://localhost:8080/SmartCampusAPI/api/v1/sensors" \
-H "Content-Type: application/json" \
-d '{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 0,
  "roomId": "LIB-301"
}'
```

### 6. Get all sensors

```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors"
```

### 7. Filter sensors by type

```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Temperature"
```

### 8. Add a sensor reading

```bash
curl -X POST "http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings" \
-H "Content-Type: application/json" \
-d '{
  "timestamp": 1713270000000,
  "value": 22.5
}'
```

### 9. Get sensor readings

```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings"
```

### 10. Error test: invalid room reference

```bash
curl -X POST "http://localhost:8080/SmartCampusAPI/api/v1/sensors" \
-H "Content-Type: application/json" \
-d '{
  "id": "CO2-001",
  "type": "CO2",
  "status": "ACTIVE",
  "currentValue": 0,
  "roomId": "BAD-ROOM"
}'
```

---

## 6. Project Structure

```text
src/main/java/com/senadhi/smartcampusapi
│
├── JAXRSConfiguration.java
│
├── model
│   ├── Room.java
│   ├── Sensor.java
│   ├── SensorReading.java
│   └── ErrorMessage.java
│
├── store
│   └── DataStore.java
│
├── resources
│   ├── DiscoveryResource.java
│   ├── RoomResource.java
│   ├── SensorResource.java
│   └── SensorReadingResource.java
│
├── exception
│   ├── RoomNotEmptyException.java
│   ├── LinkedResourceNotFoundException.java
│   ├── SensorUnavailableException.java
│   ├── RoomNotEmptyExceptionMapper.java
│   ├── LinkedResourceNotFoundExceptionMapper.java
│   ├── SensorUnavailableExceptionMapper.java
│   └── GenericExceptionMapper.java
│
└── filter
    └── ApiLoggingFilter.java
```

---

## 7. Conceptual Report Answers

### Part 1.1 – Default Lifecycle of a JAX-RS Resource Class
By default, a JAX-RS resource class is usually **request-scoped**, meaning a new instance is created for each incoming HTTP request. This is useful because request-specific state is not shared between clients. However, in this coursework the actual application data is stored in shared in-memory collections such as `HashMap` and `ArrayList`. Because those structures are shared across requests, they must be managed carefully to avoid race conditions, inconsistent updates or accidental data loss if multiple requests access them at the same time.

### Part 1.2 – Why Hypermedia / HATEOAS is Useful
Hypermedia is useful because it allows clients to discover the API through links returned in responses instead of relying only on fixed external documentation. In this project, the discovery endpoint returns links to important collections such as `/api/v1/rooms` and `/api/v1/sensors`. This benefits client developers because they can navigate the API dynamically, reduce hard coded assumptions and adapt more easily if the API is extended in the future.

### Part 2.1 – Returning IDs Only vs Full Room Objects
Returning only room IDs reduces the size of the response and saves bandwidth, which is useful when many rooms exist. However, it forces the client to make extra requests to fetch more details. Returning full room objects increases the response size, but it is more convenient for the client because the data is immediately available. In this implementation, returning full room objects is more practical because it simplifies testing and reduces extra client side processing.

### Part 2.2 – Is DELETE Idempotent?
Yes, DELETE is idempotent because repeating the same DELETE request does not create additional side effects beyond the first successful deletion. In this implementation, the first DELETE removes the room if the business rules allow it. If the same request is sent again afterwards, the room is already gone, so the server may return “Room not found,” but the final state of the system remains unchanged after the first deletion.

### Part 3.1 – Effect of `@Consumes(MediaType.APPLICATION_JSON)`
The `@Consumes(MediaType.APPLICATION_JSON)` annotation means that the method is designed to accept JSON request bodies only. If a client sends a different content type such as `text/plain` or `application/xml`, the JAX-RS runtime will not match the request correctly to that method and should reject it as an unsupported media type. This makes the API stricter and safer because it only accepts the format the method was designed to process.

### Part 3.2 – Why `@QueryParam` Is Better for Filtering
Using `@QueryParam`, for example `/sensors?type=CO2`, is more suitable for filtering because the client is still requesting the same collection resource, only with an optional condition applied. A path such as `/sensors/type/CO2` makes the filter appear like a separate resource. Query parameters are therefore more flexible and more RESTful for searching and filtering collection results.

### Part 4.1 – Benefits of the Sub-Resource Locator Pattern
The Sub Resource Locator pattern improves structure and maintainability by delegating nested logic to a separate class. Instead of placing all sensor and reading operations in one large resource class, `SensorResource` delegates reading related work to `SensorReadingResource`. This keeps responsibilities separate, makes the code easier to understand, reduces clutter and makes future extension easier.

### Part 5.2 – Why 422 Is More Accurate Than 404
`422 Unprocessable Entity` is more accurate than `404 Not Found` because the endpoint `/sensors` does exist and the JSON body itself may be syntactically valid. The problem is semantic: the `roomId` inside the payload refers to a room that does not exist. A `404` normally describes a missing target resource in the request URL, whereas `422` better describes a valid request body that cannot be processed correctly due to invalid linked data.

### Part 5.4 – Why Internal Stack Traces Should Not Be Exposed
Internal Java stack traces should not be exposed because they can reveal sensitive implementation details such as package names, class names, method names, library versions, file paths and internal control flow. An attacker could use this information to understand the internal structure of the system, identify weak points or target known vulnerabilities in specific frameworks or libraries. Returning a generic `500 Internal Server Error` response is therefore much safer.

### Part 5.5 – Why Filters Are Better for Logging
Filters are better for logging because logging is a cross cutting concern that applies to many endpoints. By using JAX-RS filters, the request and response logging logic is placed in one central component instead of being repeated inside every resource method. This reduces duplication, keeps resource classes cleaner and makes the logging behavior more consistent and easier to maintain.

---

## 8. Exception Handling Implemented

This API uses custom exception classes and exception mappers for the required error scenarios:

* `RoomNotEmptyException` → `409 Conflict`
* `LinkedResourceNotFoundException` → `422 Unprocessable Entity`
* `SensorUnavailableException` → `403 Forbidden`
* `GenericExceptionMapper` → `500 Internal Server Error`

This ensures the API is leak proof and does not expose raw Java stack traces or default Tomcat error pages to clients. 

---

## 9. Logging Implemented

The project includes `ApiLoggingFilter`, which implements:

* `ContainerRequestFilter`
* `ContainerResponseFilter`

It logs:

* incoming HTTP method and URI
* outgoing HTTP response status code

This improves observability and supports debugging and monitoring. 

---

## 10. Important Notes

* This project uses **JAX-RS only**.
* No Spring Boot or other framework has been used.
* No database has been used.
* Data is stored using in-memory collections only.
* Restarting the server clears the stored data. 

---

## 11. Video Demonstration

The API was tested using Postman. The demonstration includes:

* discovery endpoint
* room creation and lookup
* sensor creation and filtering
* nested sensor readings
* error handling scenarios
* logging output in NetBeans
