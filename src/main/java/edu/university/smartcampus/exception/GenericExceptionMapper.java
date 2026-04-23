package edu.university.smartcampus.exception;

import edu.university.smartcampus.model.ApiError;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof BadRequestException badRequestException) {
            ApiError error = new ApiError(400, "Bad Request", badRequestException.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error)
                    .build();
        }

        if (exception instanceof WebApplicationException webApplicationException) {
            Response original = webApplicationException.getResponse();
            if (original != null && original.getEntity() != null) {
            return Response.fromResponse(original)
                .type(MediaType.APPLICATION_JSON)
                .build();
            }

            int status = original == null ? 500 : original.getStatus();
            ApiError error = new ApiError(status, "HTTP Error", webApplicationException.getMessage());
            return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
        }

        ApiError error = new ApiError(500, "Internal Server Error", "Unexpected server error.");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
