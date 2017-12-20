package ru.faulab.javaee.design.patterns.sample.project.platform.impl.exception;

import com.google.common.base.Throwables;
import ru.faulab.javaee.design.patterns.sample.project.platform.expection.ErrorValueObject;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnexpectedExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {

        return handleAcceptType(Response.serverError()).entity(
                ErrorValueObject.builder()
                        .errorCode(1)
                        .userMessage("Oops. Contact with your administrator")
                        .developerMessage(Throwables.getStackTraceAsString(exception)) /*logging OoS*/
                        .build())
                .build();
    }
}
