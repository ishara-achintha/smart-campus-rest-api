package com.mycompany.achintha20232551.resource;

import com.mycompany.achintha20232551.exception.SensorUnavailableException;
import com.mycompany.achintha20232551.model.Sensor;
import com.mycompany.achintha20232551.model.SensorReading;
import com.mycompany.achintha20232551.store.DataStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/sensors/{sensorId}/readings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource() {
        this.sensorId = null;
    }

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReadings() {
        Sensor sensor = getSensorOrThrow();
        List<SensorReading> readings = DataStore.getReadings().get(sensor.getId());
        if (readings == null) {
            return new ArrayList<>();
        }
        return readings;
    }

    @POST
    public Response addReading(SensorReading reading, @Context UriInfo uriInfo) {
        Sensor sensor = getSensorOrThrow();

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor '" + sensor.getId() + "' is under maintenance and cannot accept new readings.");
        }

        validateReading(reading);

        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() <= 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        DataStore.getOrCreateReadings(sensor.getId()).add(reading);
        sensor.setCurrentValue(reading.getValue());

        URI location = uriInfo.getAbsolutePathBuilder().path(reading.getId()).build();
        return Response.created(location).entity(reading).build();
    }

    private Sensor getSensorOrThrow() {
        if (sensorId == null || sensorId.trim().isEmpty()) {
            throw new NotFoundException("Sensor ID is missing.");
        }
        Sensor sensor = DataStore.getSensors().get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not found for ID: " + sensorId);
        }
        return sensor;
    }

    private void validateReading(SensorReading reading) {
        if (reading == null) {
            throw new BadRequestException("Request body is required.");
        }
        if (Double.isNaN(reading.getValue()) || Double.isInfinite(reading.getValue())) {
            throw new BadRequestException("Reading value must be a valid number.");
        }
    }
}
