# Smart Campus REST API

> **Student Name** – Ishara Achintha  
> **UoW Number** – w2120611
> **IIT Number** – 20232551

---

## Project Overview

This project implements a RESTful Smart Campus API using Java, JAX-RS (Jersey), and an embedded Grizzly server.

The system manages:

- Rooms  
- Sensors  
- Sensor Readings  

The implementation follows all coursework constraints:

- Uses JAX-RS only  
- Uses embedded Grizzly server  
- Uses in-memory data structures  
- No database used  
- No external frameworks  

---

## System Architecture

The API follows a layered architecture:

- Resource Layer  
- Service/Data Layer  
- Model Layer  

A Singleton DataStore is used to persist data across requests.

---

## JAX-RS Resource Lifecycle

JAX-RS uses a request-scoped lifecycle.

- A new resource instance is created per request  
- Instance variables are not shared  

A shared datastore is used to persist data across requests.

---

## HATEOAS Implementation

The API includes basic hypermedia links in responses.

Example:

{
  "id": "ROOM-001",
  "name": "Lab 1",
  "links": {
    "self": "/api/v1/rooms/ROOM-001",
    "sensors": "/api/v1/rooms/ROOM-001/sensors"
  }
}

---

## Base URL

http://localhost:9090/api/v1

---

## API Features

Rooms, Sensors, Sensor Readings with CRUD operations.

---

## Validation Rules

Room, Sensor, and Reading validations implemented.

---

## Error Handling

All errors return JSON format.

---

## Build and Run Instructions

git clone https://github.com/ishara-achintha/smart-campus-rest-api.git
cd smart-campus-rest-api
mvn clean install
mvn exec:java

---

## Testing

Tested using Postman and curl.

---

## Technologies Used

Java, Maven, JAX-RS, Grizzly

---

## Limitations

- In-memory storage  
- No persistence  
- Limited concurrency  

---

## Future Improvements

- Database integration  
- Authentication  
- Better concurrency handling  

---

## Conclusion

This project demonstrates a RESTful API built with core Java technologies.
