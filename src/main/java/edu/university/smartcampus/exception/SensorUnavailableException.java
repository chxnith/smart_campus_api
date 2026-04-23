package edu.university.smartcampus.exception;

public class SensorUnavailableException extends RuntimeException {

    private final String sensorId;

    public SensorUnavailableException(String sensorId) {
        super("Sensor is not accepting readings in its current state: " + sensorId);
        this.sensorId = sensorId;
    }

    public String getSensorId() {
        return sensorId;
    }
}
