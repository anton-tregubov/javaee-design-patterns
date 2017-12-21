package ru.faulab.javaee.design.patterns.sample.project.platform.impl.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JacksonConfiguration implements ContextResolver<ObjectMapper> {
    private final ObjectMapper objectMapper;

    public JacksonConfiguration() {
        this.objectMapper = new ObjectMapper()
                .findAndRegisterModules()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.WRAP_EXCEPTIONS, false)
                .configure(SerializationFeature.WRAP_EXCEPTIONS, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public ObjectMapper getContext(Class<?> objectType) {
        return objectMapper;
    }
}
