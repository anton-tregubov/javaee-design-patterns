package ru.faulab.javaee.design.patterns.sample.project.web;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.annotation.Priority;
import javax.ws.rs.Consumes;
import javax.ws.rs.Priorities;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

//need for glassfish and tomee
@Priority(Priorities.ENTITY_CODER)
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PriorityJacksonJsonProvider extends JacksonJsonProvider {
}
