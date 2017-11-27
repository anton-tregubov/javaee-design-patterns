package ru.faulab.javaee.design.patterns.sample.project.note.web.rest;

import ru.faulab.javaee.design.patterns.sample.project.note.Note;
import ru.faulab.javaee.design.patterns.sample.project.note.NoteFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class NotesResource {
    @Inject
    private NoteFacade noteFacade;

    @GET
    public List<Note> getAllNotes() {
        return io.vavr.collection.List.ofAll(noteFacade.getAllNotes()).asJava();
    }

    @Path("{id}")
    @GET
    public Note getNote(@PathParam("id") String id) {
        return noteFacade.findById(id);
    }

    @Path("{id}")
    @DELETE
    public void deleteNote(@PathParam("id") String id) {
        noteFacade.deleteNote(id);
    }

    @Path("{text}")
    @POST
    public Note createNote(@PathParam("text") String text) {
        return noteFacade.createNote(text);
    }

    @Path("{id}")
    @PUT
    public Note updateNote(@Valid Note note) {
        return noteFacade.updateNote(note);
    }
}
