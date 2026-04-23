package edu.university.smartcampus.api;

import edu.university.smartcampus.dto.DiscoveryResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public DiscoveryResponse getDiscovery(@Context UriInfo uriInfo, @QueryParam("crash") Boolean crash) {
        if (Boolean.TRUE.equals(crash)) {
            throw new RuntimeException("Simulated 500 for Part 5 validation");
        }
        String base = uriInfo.getBaseUri().toString();

        DiscoveryResponse response = new DiscoveryResponse();
        response.setApiName("Smart Campus API");
        response.setVersion("v1");
        response.setContact("facilities-api@university.edu");

        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("rooms", base + "rooms");
        resources.put("sensors", base + "sensors");
        resources.put("sensorReadingsTemplate", base + "sensors/{sensorId}/readings");
        response.setResources(resources);

        Map<String, String> links = new LinkedHashMap<>();
        links.put("self", base);
        links.put("createRoom", base + "rooms");
        links.put("createSensor", base + "sensors");
        response.setLinks(links);

        return response;
    }
}
