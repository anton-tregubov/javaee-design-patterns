package ru.faulab.javaee.design.patterns.sample.project.note.web.rest;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.parameters.RequestBody;
import io.swagger.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Get all notes", description = "Get list of notes",
            responses = @ApiResponse(
                    /*hope it will be auto*/content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Note.class))))
    public Iterable<Note> getAllNotes() {
        return noteFacade.getAllNotes();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Get note by id", description = "Get note by id",
            responses = {
                    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Note.class))),
                    @ApiResponse(description = "Note not Found", responseCode = "404", content = @Content(mediaType = MediaType.TEXT_PLAIN))
            })
    public Note getNote(@Parameter(name = "id", required = true, example = "42") @PathParam("id") String id) {
        Note note = noteFacade.findById(id);
        if (note == null) {
            throw new NotFoundException(String.format(Locale.ENGLISH, "'%1$s' not found", id));
        }
        return note;
    }

    @Path("{id}")
    @DELETE
    @Operation(summary = "Remove note by id", description = "Remove note by Id",
            responses = @ApiResponse(responseCode = "204", description = "removed"))
    public void deleteNote(@Parameter(name = "id", required = true, example = "42") @PathParam("id") String id) {
        noteFacade.deleteNote(id);
    }

    @Path("{text}")
    @POST
    @Operation(summary = "Create note", description = "Create note with content",
            responses = @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Note.class)))
    )
    public Note createNote(@Parameter(name = "text", required = true, example = "hi") @PathParam("text") String text) {
        return noteFacade.createNote(text);
    }

    @Path("{id}")
    @PUT
    @Operation(summary = "Update note", description = "Update note by id with content",
            responses = {
                    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Note.class))),
                    @ApiResponse(description = "Note not Found", responseCode = "404", content = @Content(mediaType = MediaType.TEXT_PLAIN))
            })
    public Note updateNote(@Parameter(name = "id", required = true, example = "42") @PathParam("id") String id,
                           @RequestBody(description = "parameters for update note",
                                   required = true,
                                   content = @Content(
                                           schema = @Schema(implementation = Note.class))
                           ) @Valid Note note /*todo make NoteUpdateDto*/) {
        Note updatedNote = noteFacade.updateNote(id, note.getContent());
        if (updatedNote == null) {
            throw new NotFoundException(String.format(Locale.ENGLISH, "'%1$s' not found", id));
        }
        return updatedNote;
    }
}
