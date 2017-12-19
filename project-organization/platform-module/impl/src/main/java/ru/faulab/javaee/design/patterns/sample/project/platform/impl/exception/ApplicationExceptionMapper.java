package ru.faulab.javaee.design.patterns.sample.project.platform.impl.exception;

import ru.faulab.javaee.design.patterns.sample.project.platform.expection.SimpleProjectException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<SimpleProjectException> {

    @Override
    public Response toResponse(SimpleProjectException exception) {
        return handleAcceptType(Response.serverError()).entity(exception.toErrorValueObject()).build();
    }
}
