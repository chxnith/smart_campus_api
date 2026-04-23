package edu.university.smartcampus.api;

import edu.university.smartcampus.model.Room;
import edu.university.smartcampus.model.Sensor;
import edu.university.smartcampus.store.InMemoryCampusStore;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final InMemoryCampusStore store = InMemoryCampusStore.getInstance();

    @GET
    public List<Room> listRooms() {
        return store.listRooms();
    }

    @GET
    @Path("/{roomId}")
    public Room getRoom(@PathParam("roomId") String roomId) {
        return store.getRoom(roomId);
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        Room created = store.createRoom(room);
        URI location = uriInfo.getAbsolutePathBuilder().path(created.getId()).build();
        return Response.created(location).entity(created).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        store.deleteRoom(roomId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{roomId}/sensors")
    public List<Sensor> listSensorsInRoom(@PathParam("roomId") String roomId) {
        return store.listRoomSensors(roomId);
    }
}
