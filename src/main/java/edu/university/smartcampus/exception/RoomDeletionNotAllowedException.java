package edu.university.smartcampus.exception;

import edu.university.smartcampus.model.ApiError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RoomDeletionNotAllowedException extends WebApplicationException {

    public RoomDeletionNotAllowedException(String roomId) {
        super(Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ApiError(
                        409,
                        "RoomDeletionNotAllowed",
                        "Room cannot be deleted while it has ACTIVE sensors assigned: " + roomId
                ))
                .build());
    }
}
