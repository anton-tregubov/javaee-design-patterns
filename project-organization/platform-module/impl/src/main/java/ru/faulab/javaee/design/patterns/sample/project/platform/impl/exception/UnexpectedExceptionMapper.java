package ru.faulab.javaee.design.patterns.sample.project.platform.impl.exception;

import ru.faulab.javaee.design.patterns.sample.project.platform.expection.ErrorValueObject;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;

@Provider
public class UnexpectedExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {

        StringWriter errorStackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(errorStackTrace));

        return handleAcceptType(Response.serverError()).entity(
                ErrorValueObject.builder()
                        .errorCode(1)
                        .userMessage("Oops. Contact with your administrator")
                        .developerMessage(errorStackTrace.toString()) /*logging OoS*/
                        .build())
                .build();
    }
}
