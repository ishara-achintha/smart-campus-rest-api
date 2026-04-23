package com.mycompany.achintha20232551.resource;

import com.mycompany.achintha20232551.exception.RoomNotEmptyException;
import com.mycompany.achintha20232551.model.Room;
import com.mycompany.achintha20232551.store.DataStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public List<Room> getAllRooms() {
        return new ArrayList<>(DataStore.getRooms().values());
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        validateRoom(room);

        room.setId(room.getId().trim());
        room.setName(room.getName().trim());

        Map<String, Room> rooms = DataStore.getRooms();
        if (rooms.containsKey(room.getId())) {
            throw new BadRequestException("Room with ID '" + room.getId() + "' already exists.");
        }

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        rooms.put(room.getId(), room);
        URI location = uriInfo.getAbsolutePathBuilder().path(room.getId()).build();
        return Response.created(location).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Room getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRooms().get(roomId);
        if (room == null) {
            throw new NotFoundException("Room not found for ID: " + roomId);
        }
        return room;
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRooms().get(roomId);
        if (room == null) {
            throw new NotFoundException("Room not found for ID: " + roomId);
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room '" + roomId + "' cannot be deleted because sensors are still assigned to it.");
        }

        DataStore.getRooms().remove(roomId);
        return Response.ok(new MessageResponse("Room deleted successfully.")).build();
    }

    private void validateRoom(Room room) {
        if (room == null) {
            throw new BadRequestException("Request body is required.");
        }
        if (isBlank(room.getId())) {
            throw new BadRequestException("Room ID is required.");
        }
        if (isBlank(room.getName())) {
            throw new BadRequestException("Room name is required.");
        }
        if (room.getCapacity() <= 0) {
            throw new BadRequestException("Room capacity must be greater than 0.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static class MessageResponse {

        private String message;

        public MessageResponse() {
        }

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
