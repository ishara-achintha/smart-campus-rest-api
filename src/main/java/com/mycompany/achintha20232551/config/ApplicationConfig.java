package com.mycompany.achintha20232551.config;

import com.mycompany.achintha20232551.filter.ApiLoggingFilter;
import com.mycompany.achintha20232551.mapper.LinkedResourceNotFoundExceptionMapper;
import com.mycompany.achintha20232551.mapper.RoomNotEmptyExceptionMapper;
import com.mycompany.achintha20232551.mapper.SensorUnavailableExceptionMapper;
import com.mycompany.achintha20232551.mapper.ThrowableExceptionMapper;
import com.mycompany.achintha20232551.resource.DiscoveryResource;
import com.mycompany.achintha20232551.resource.RoomResource;
import com.mycompany.achintha20232551.resource.SensorResource;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        register(DiscoveryResource.class);
        register(RoomResource.class);
        register(SensorResource.class);

        register(RoomNotEmptyExceptionMapper.class);
        register(LinkedResourceNotFoundExceptionMapper.class);
        register(SensorUnavailableExceptionMapper.class);
        register(ThrowableExceptionMapper.class);

        register(ApiLoggingFilter.class);
        register(JacksonFeature.class);
    }
}
