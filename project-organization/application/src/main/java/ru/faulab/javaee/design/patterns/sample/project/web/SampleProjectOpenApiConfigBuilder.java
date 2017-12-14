package ru.faulab.javaee.design.patterns.sample.project.web;

import io.swagger.oas.integration.SwaggerConfiguration;
import io.swagger.oas.integration.api.OpenAPIConfigBuilder;
import io.swagger.oas.integration.api.OpenAPIConfiguration;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.servers.Server;
import io.vavr.collection.HashSet;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletContext;
import java.util.Collections;

public class SampleProjectOpenApiConfigBuilder implements OpenAPIConfigBuilder {
    @Override
    public OpenAPIConfiguration build() {
        return new SwaggerConfiguration()
                .prettyPrint(true)
                .resourcePackages(HashSet.of("ru.faulab.javaee.design.patterns.sample.project").toJavaSet())
                .openAPI(
                        new OpenAPI().info(
                                new Info()
                                        .title("Simple Project Api")
                                        .version(getClass().getPackage().getImplementationVersion())
                        )
                                .servers(Collections.singletonList(new Server().description("Current Server").url(CDI.current().select(ServletContext.class).get().getContextPath() + "/" + SampleProjectApplication.GLOBAL_REST_ADI_PREFIX)))
                );
    }
}
