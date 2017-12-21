package ru.faulab.javaee.design.patterns.sample.project.platform.impl.jackson;

import com.fasterxml.jackson.jaxrs.base.JsonMappingExceptionMapper;
import com.fasterxml.jackson.jaxrs.base.JsonParseExceptionMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Priorities;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.util.Locale;

@Provider
public class JacksonActivator implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        final Configuration config = context.getConfiguration();
        context.property("jersey.config." + config.getRuntimeType().toString().toLowerCase(Locale.ENGLISH) + ".jsonFeature", "JacksonFeature");
        context.register(JsonParseExceptionMapper.class, Priorities.ENTITY_CODER);
        context.register(JsonMappingExceptionMapper.class, Priorities.ENTITY_CODER);
        context.register(JacksonJsonProviderForJsonMediaType.class, Priorities.ENTITY_CODER);
        return true;
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public static class JacksonJsonProviderForJsonMediaType extends JacksonJsonProvider {

    }
}
