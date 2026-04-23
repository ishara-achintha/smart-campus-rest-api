# Achintha 20232551 - Smart Campus REST API

This project is a JAX-RS RESTful API for a Smart Campus system. It manages rooms, sensors, and sensor readings using in-memory Java collections only.

## Technology Stack
- Java 17
- Maven
- JAX-RS with Jersey
- Grizzly embedded HTTP server
- In-memory storage with `ConcurrentHashMap` and `ArrayList`

## Run the project in NetBeans
1. Open the project in NetBeans.
2. Wait until Maven dependencies finish loading.
3. Open `Main.java`.
4. Right click `Main.java`.
5. Click **Run File**.
6. The server starts on:
   `http://localhost:9090/api/v1/`

## Important Postman rule
- `GET` requests do not use a body.
- `POST` requests use JSON in the body.
- Set header `Content-Type: application/json` for `POST` requests.

## Main endpoints
- `GET /api/v1`
- `GET /api/v1/rooms`
- `POST /api/v1/rooms`
- `GET /api/v1/rooms/{roomId}`
- `DELETE /api/v1/rooms/{roomId}`
- `GET /api/v1/sensors`
- `GET /api/v1/sensors?type=CO2`
- `POST /api/v1/sensors`
- `GET /api/v1/sensors/{sensorId}`
- `GET /api/v1/sensors/{sensorId}/readings`
- `POST /api/v1/sensors/{sensorId}/readings`

## Sample curl commands
Create a room:
```bash
curl -X POST http://localhost:9090/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LAB-202","name":"AI Lab","capacity":40}'
```

Get all rooms:
```bash
curl http://localhost:9090/api/v1/rooms
```

Create a sensor:
```bash
curl -X POST http://localhost:9090/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-001","type":"CO2","status":"ACTIVE","currentValue":410.2,"roomId":"LAB-202"}'
```

Filter sensors:
```bash
curl http://localhost:9090/api/v1/sensors?type=CO2
```

Add a reading:
```bash
curl -X POST http://localhost:9090/api/v1/sensors/CO2-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":430.8}'
```

Get reading history:
```bash
curl http://localhost:9090/api/v1/sensors/CO2-001/readings
```

## Expected validation and error handling
- 409 Conflict when deleting a room that still has sensors.
- 422 Unprocessable Entity when creating a sensor for a room that does not exist.
- 403 Forbidden when posting readings to a sensor in `MAINTENANCE` state.
- 400 Bad Request for invalid input.
- 500 Internal Server Error for unexpected failures.


## Important Note About the Base URL

This project runs with the Grizzly base URI set directly to `http://localhost:9090/api/v1/` in `Main.java`.
If you change it back to `http://localhost:9090/`, your endpoints will return 404 in the browser and Postman.
