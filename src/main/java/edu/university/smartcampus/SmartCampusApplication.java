package edu.university.smartcampus;

import edu.university.smartcampus.api.DiscoveryResource;
import edu.university.smartcampus.api.RoomResource;
import edu.university.smartcampus.api.SensorResource;
import edu.university.smartcampus.exception.BadRequestExceptionMapper;
import edu.university.smartcampus.exception.GlobalExceptionMapper;
import edu.university.smartcampus.exception.LinkedResourceNotFoundExceptionMapper;
import edu.university.smartcampus.exception.NotFoundExceptionMapper;
import edu.university.smartcampus.exception.RoomNotEmptyExceptionMapper;
import edu.university.smartcampus.exception.SensorUnavailableExceptionMapper;
import edu.university.smartcampus.filter.ApiLoggingFilter;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DiscoveryResource.class);
        classes.add(RoomResource.class);
        classes.add(SensorResource.class);
        classes.add(ApiLoggingFilter.class);
        classes.add(BadRequestExceptionMapper.class);
        classes.add(NotFoundExceptionMapper.class);
        classes.add(RoomNotEmptyExceptionMapper.class);
        classes.add(LinkedResourceNotFoundExceptionMapper.class);
        classes.add(SensorUnavailableExceptionMapper.class);
        classes.add(GlobalExceptionMapper.class);
        return classes;
    }
}
