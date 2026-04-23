package com.mycompany.achintha20232551.resource;

import com.mycompany.achintha20232551.exception.LinkedResourceNotFoundException;
import com.mycompany.achintha20232551.model.Room;
import com.mycompany.achintha20232551.model.Sensor;
import com.mycompany.achintha20232551.store.DataStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public List<Sensor> getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<>(DataStore.getSensors().values());
        if (type == null || type.trim().isEmpty()) {
            return sensors;
        }
        return sensors.stream()
                .filter(sensor -> sensor.getType() != null && sensor.getType().equalsIgnoreCase(type.trim()))
                .collect(Collectors.toList());
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        validateSensor(sensor);

        Map<String, Sensor> sensors = DataStore.getSensors();
        if (sensors.containsKey(sensor.getId())) {
            throw new BadRequestException("Sensor with ID '" + sensor.getId() + "' already exists.");
        }

        Room room = DataStore.getRooms().get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException("Room with ID '" + sensor.getRoomId() + "' does not exist.");
        }

        sensor.setId(sensor.getId().trim());
        sensor.setType(sensor.getType().trim());
        sensor.setStatus(sensor.getStatus().trim().toUpperCase(Locale.ROOT));
        sensor.setRoomId(sensor.getRoomId().trim());

        sensors.put(sensor.getId(), sensor);
        if (!room.getSensorIds().contains(sensor.getId())) {
            room.getSensorIds().add(sensor.getId());
        }
        DataStore.getOrCreateReadings(sensor.getId());

        URI location = uriInfo.getAbsolutePathBuilder().path(sensor.getId()).build();
        return Response.created(location).entity(sensor).build();
    }

    @GET
    @Path("/{sensorId}")
    public Sensor getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.getSensors().get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not found for ID: " + sensorId);
        }
        return sensor;
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        if (!DataStore.getSensors().containsKey(sensorId)) {
            throw new NotFoundException("Sensor not found for ID: " + sensorId);
        }
        return new SensorReadingResource(sensorId);
    }

    private void validateSensor(Sensor sensor) {
        if (sensor == null) {
            throw new BadRequestException("Request body is required.");
        }
        if (isBlank(sensor.getId())) {
            throw new BadRequestException("Sensor ID is required.");
        }
        if (isBlank(sensor.getType())) {
            throw new BadRequestException("Sensor type is required.");
        }
        if (isBlank(sensor.getStatus())) {
            throw new BadRequestException("Sensor status is required.");
        }
        if (isBlank(sensor.getRoomId())) {
            throw new BadRequestException("Sensor roomId is required.");
        }

        String normalizedStatus = sensor.getStatus().trim().toUpperCase(Locale.ROOT);
        if (!normalizedStatus.equals("ACTIVE") && !normalizedStatus.equals("MAINTENANCE") && !normalizedStatus.equals("OFFLINE")) {
            throw new BadRequestException("Sensor status must be ACTIVE, MAINTENANCE, or OFFLINE.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
