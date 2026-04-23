package com.mycompany.achintha20232551.resource;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Map<String, Object> discover() {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, String> links = new LinkedHashMap<>();

        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");

        response.put("name", "Smart Campus API");
        response.put("version", "v1");
        response.put("adminContact", "admin@smartcampus.local");
        response.put("resources", links);
        return response;
    }
}
