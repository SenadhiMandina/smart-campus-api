/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senadhi.smartcampusapi;

/**
 *
 * @author senadhimandina
 */

import com.senadhi.smartcampusapi.exception.GlobalExceptionMapper;
import com.senadhi.smartcampusapi.exception.LinkedResourceNotFoundExceptionMapper;
import com.senadhi.smartcampusapi.exception.RoomNotEmptyExceptionMapper;
import com.senadhi.smartcampusapi.exception.SensorUnavailableExceptionMapper;
import com.senadhi.smartcampusapi.filter.ApiLoggingFilter;
import com.senadhi.smartcampusapi.resources.DebugResource;
import com.senadhi.smartcampusapi.resources.DiscoveryResource;
import com.senadhi.smartcampusapi.resources.RoomResource;
import com.senadhi.smartcampusapi.resources.SensorReadingResource;
import com.senadhi.smartcampusapi.resources.SensorResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api/v1")
public class JAXRSConfiguration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DiscoveryResource.class);
        classes.add(RoomResource.class);
        classes.add(SensorResource.class);
        classes.add(DebugResource.class);

        classes.add(RoomNotEmptyExceptionMapper.class);
        classes.add(LinkedResourceNotFoundExceptionMapper.class);
        classes.add(SensorUnavailableExceptionMapper.class);
        classes.add(GlobalExceptionMapper.class);

        classes.add(ApiLoggingFilter.class);

        return classes;
    }
}