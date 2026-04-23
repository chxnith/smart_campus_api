package edu.university.smartcampus.api;

import edu.university.smartcampus.model.SensorReading;
import edu.university.smartcampus.store.InMemoryCampusStore;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;
    private final InMemoryCampusStore store;

    public SensorReadingResource(String sensorId, InMemoryCampusStore store) {
        this.sensorId = sensorId;
        this.store = store;
    }

    @GET
    public List<SensorReading> listReadings() {
        return store.listReadings(sensorId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading, @Context UriInfo uriInfo) {
        SensorReading created = store.addReading(sensorId, reading);
        URI location = uriInfo.getAbsolutePathBuilder().path(created.getId()).build();
        return Response.created(location).entity(created).build();
    }
}
