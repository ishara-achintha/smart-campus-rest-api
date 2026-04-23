package com.mycompany.achintha20232551.mapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            WebApplicationException webException = (WebApplicationException) exception;
            Response original = webException.getResponse();
            String reason = original.getStatusInfo().getReasonPhrase();
            ErrorResponse error = new ErrorResponse(reason, exception.getMessage() == null ? reason : exception.getMessage(), original.getStatus());
            return Response.status(original.getStatus())
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error)
                    .build();
        }

        ErrorResponse error = new ErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred. Please contact the administrator.",
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
