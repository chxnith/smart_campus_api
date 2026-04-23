package edu.university.smartcampus.store;

import edu.university.smartcampus.model.Room;
import edu.university.smartcampus.model.Sensor;
import edu.university.smartcampus.model.SensorReading;
import edu.university.smartcampus.exception.LinkedResourceNotFoundException;
import edu.university.smartcampus.exception.RoomNotEmptyException;
import edu.university.smartcampus.exception.SensorUnavailableException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class InMemoryCampusStore {

    private static final InMemoryCampusStore INSTANCE = new InMemoryCampusStore();

    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private final Map<String, CopyOnWriteArrayList<SensorReading>> sensorReadings = new ConcurrentHashMap<>();

    private InMemoryCampusStore() {
        seedData();
    }

    public static InMemoryCampusStore getInstance() {
        return INSTANCE;
    }

    public List<Room> listRooms() {
        return new ArrayList<>(rooms.values());
    }

    public Room getRoom(String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new NotFoundException("Room not found: " + roomId);
        }
        return room;
    }

    public synchronized Room createRoom(Room room) {
        if (room == null || isBlank(room.getId()) || isBlank(room.getName())) {
            throw new BadRequestException("Room id and name are required.");
        }
        if (rooms.containsKey(room.getId())) {
            throw new BadRequestException("Room already exists: " + room.getId());
        }
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }
        rooms.put(room.getId(), room);
        return room;
    }

    public synchronized void deleteRoom(String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new NotFoundException("Room not found: " + roomId);
        }

        for (String sensorId : room.getSensorIds()) {
            Sensor sensor = sensors.get(sensorId);
            if (sensor != null && sensor.getStatus() != null && !"OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
                throw new RoomNotEmptyException(roomId);
            }
        }

        Room removed = rooms.remove(roomId);

        for (String sensorId : removed.getSensorIds()) {
            sensors.remove(sensorId);
            sensorReadings.remove(sensorId);
        }
    }

    public List<Sensor> listSensors() {
        return new ArrayList<>(sensors.values());
    }

    public List<Sensor> listSensorsByType(String type) {
        if (isBlank(type)) {
            return listSensors();
        }

        List<Sensor> result = new ArrayList<>();
        for (Sensor sensor : sensors.values()) {
            if (sensor != null && sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                result.add(sensor);
            }
        }
        return result;
    }

    public List<Sensor> listRoomSensors(String roomId) {
        Room room = getRoom(roomId);
        List<Sensor> result = new ArrayList<>();
        for (String sensorId : room.getSensorIds()) {
            Sensor sensor = sensors.get(sensorId);
            if (sensor != null) {
                result.add(sensor);
            }
        }
        return result;
    }

    public Sensor getSensor(String sensorId) {
        Sensor sensor = sensors.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not found: " + sensorId);
        }
        return sensor;
    }

    public synchronized Sensor createSensor(Sensor sensor) {
        if (sensor == null || isBlank(sensor.getId()) || isBlank(sensor.getType()) || isBlank(sensor.getRoomId())) {
            throw new BadRequestException("Sensor id, type and roomId are required.");
        }
        if (sensors.containsKey(sensor.getId())) {
            throw new BadRequestException("Sensor already exists: " + sensor.getId());
        }

        Room room;
        try {
            room = getRoom(sensor.getRoomId());
        } catch (NotFoundException e) {
            throw new LinkedResourceNotFoundException("Room does not exist for sensor.roomId: " + sensor.getRoomId());
        }

        sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());
        sensorReadings.put(sensor.getId(), new CopyOnWriteArrayList<>());
        return sensor;
    }

    public synchronized SensorReading addReading(String sensorId, SensorReading reading) {
        Sensor sensor = getSensor(sensorId);
        if (sensor.getStatus() != null && "MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId);
        }
        if (reading == null) {
            throw new BadRequestException("Reading body is required.");
        }

        if (isBlank(reading.getId())) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() == 0L) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        sensor.setCurrentValue(reading.getValue());
        sensorReadings.computeIfAbsent(sensorId, s -> new CopyOnWriteArrayList<>()).add(reading);
        return reading;
    }

    public List<SensorReading> listReadings(String sensorId) {
        getSensor(sensorId);
        return new ArrayList<>(sensorReadings.getOrDefault(sensorId, new CopyOnWriteArrayList<>()));
    }

    private void seedData() {
        Room lib = new Room("LIB-301", "Library Quiet Study", 120);
        Room eng = new Room("ENG-110", "Engineering Lab", 40);
        rooms.put(lib.getId(), lib);
        rooms.put(eng.getId(), eng);

        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.1, lib.getId());
        Sensor s2 = new Sensor("CO2-101", "CO2", "ACTIVE", 460, eng.getId());
        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);
        lib.getSensorIds().add(s1.getId());
        eng.getSensorIds().add(s2.getId());

        sensorReadings.put(s1.getId(), new CopyOnWriteArrayList<>());
        sensorReadings.put(s2.getId(), new CopyOnWriteArrayList<>());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
