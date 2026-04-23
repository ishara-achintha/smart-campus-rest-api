package com.mycompany.achintha20232551.store;

import com.mycompany.achintha20232551.model.Room;
import com.mycompany.achintha20232551.model.Sensor;
import com.mycompany.achintha20232551.model.SensorReading;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DataStore {

    private static final Map<String, Room> ROOMS = new ConcurrentHashMap<>();
    private static final Map<String, Sensor> SENSORS = new ConcurrentHashMap<>();
    private static final Map<String, List<SensorReading>> READINGS = new ConcurrentHashMap<>();

    private DataStore() {
    }

    public static Map<String, Room> getRooms() {
        return ROOMS;
    }

    public static Map<String, Sensor> getSensors() {
        return SENSORS;
    }

    public static Map<String, List<SensorReading>> getReadings() {
        return READINGS;
    }

    public static List<SensorReading> getOrCreateReadings(String sensorId) {
        return READINGS.computeIfAbsent(sensorId, key -> Collections.synchronizedList(new ArrayList<>()));
    }
}
