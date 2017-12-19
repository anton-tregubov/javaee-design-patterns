package ru.faulab.javaee.design.patterns.sample.project.platform.impl.exception;

import ru.faulab.javaee.design.patterns.sample.project.platform.expection.ErrorValueObject;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream().map(ConstraintViolation::toString).collect(Collectors.joining(","));
        return handleAcceptType(Response.status(Response.Status.BAD_REQUEST)).entity(
                ErrorValueObject.builder()
                        .errorCode(2)
                        .userMessage(message)
                        .build())
                .build();
    }
}
