package ru.faulab.javaee.design.patterns.sample.project.platform.impl.exception;

import javax.ws.rs.core.*;
import java.util.List;

public abstract class AbstractExceptionMapper {

    private static final List<Variant> VARIANTS = Variant.mediaTypes(
            MediaType.APPLICATION_JSON_TYPE,
            MediaType.TEXT_XML_TYPE,
            MediaType.TEXT_PLAIN_TYPE)
            .build();

    @Context
    protected Request request;

    protected Response.ResponseBuilder handleAcceptType(Response.ResponseBuilder responseBuilder) {
        Variant variant = request.selectVariant(VARIANTS);
        if (variant == null) {
            return Response.notAcceptable(VARIANTS);
        }
        return responseBuilder.variant(variant);
    }
}
