package ru.faulab.javaee.design.patterns.sample.project.note.web.rest;

import ru.faulab.javaee.design.patterns.sample.project.note.Note;
import ru.faulab.javaee.design.patterns.sample.project.note.NoteFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Locale;

@Path("notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class NotesResource {
    @Inject
    private NoteFacade noteFacade;

    @GET
    public Iterable<Note> getAllNotes() {
        return noteFacade.getAllNotes();
    }

    @Path("{id}")
    @GET
    public Note getNote(@PathParam("id") String id) {
        Note note = noteFacade.findById(id);
        if (note == null) {
            throw new NotFoundException(String.format(Locale.ENGLISH, "'%1$s' not found", id));
        }
        return note;
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
    public Note updateNote(@PathParam("id") String id, @Valid Note note /*todo make NoteUpdateDto*/) {
        Note updatedNote = noteFacade.updateNote(id, note.getContent());
        if (updatedNote == null) {
            throw new NotFoundException(String.format(Locale.ENGLISH, "'%1$s' not found", id));
        }
        return updatedNote;
    }
}
