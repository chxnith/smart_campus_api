package edu.university.smartcampus.exception;

public class RoomNotEmptyException extends RuntimeException {

    private final String roomId;

    public RoomNotEmptyException(String roomId) {
        super("Room cannot be deleted because it is currently occupied by active hardware: " + roomId);
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
