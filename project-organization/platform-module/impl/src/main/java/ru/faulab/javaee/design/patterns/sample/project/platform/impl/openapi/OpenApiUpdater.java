package ru.faulab.javaee.design.patterns.sample.project.platform.impl.openapi;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.ReaderListener;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import ru.faulab.javaee.design.patterns.sample.project.platform.expection.ErrorValueObject;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Response;
import java.util.Collections;

@OpenAPIDefinition
public class OpenApiUpdater implements ReaderListener {
    @Override
    public void beforeScan(Reader reader, OpenAPI openAPI) {
        //no need
    }

    @Override
    public void afterScan(Reader reader, OpenAPI openAPI) {
        openAPI.info(
                new Info()
                        .title("Simple Project Api")
                        .version(getClass().getPackage().getImplementationVersion())
        );
        openAPI.servers(Collections.singletonList(new Server().description("Current Server").url(CDI.current().select(ServletContext.class).get().getContextPath())));
        //error
        Schema errorObject = ModelConverters.getInstance().readAllAsResolvedSchema(ErrorValueObject.class).schema;
        openAPI.getComponents().addSchemas("Error", errorObject);
        openAPI.getPaths()
                .values()
                .stream()
                .flatMap(pathItem -> pathItem.readOperations().stream())
                .forEach(operation -> {
                    ApiResponses responses = operation.getResponses();
                    responses
                            .addApiResponse(String.valueOf(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()),
                                    new ApiResponse().description("Unexpected exception. Error code = 1")
                                            .content(
                                                    new Content().addMediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON, new MediaType()
                                                            .schema(errorObject))));
                    if (operation.getParameters() != null && !operation.getParameters().isEmpty()) {
                        responses
                                .addApiResponse(String.valueOf(Response.Status.BAD_REQUEST.getStatusCode()),
                                        new ApiResponse().description("Bad request. Error code = 2")
                                                .content(
                                                        new Content().addMediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON, new MediaType()
                                                                .schema(errorObject))));
                    }
                });
    }
}
