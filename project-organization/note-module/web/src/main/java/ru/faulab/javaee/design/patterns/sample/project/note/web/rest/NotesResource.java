package ru.faulab.javaee.design.patterns.sample.project.note.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.faulab.javaee.design.patterns.sample.project.note.Note;
import ru.faulab.javaee.design.patterns.sample.project.note.NoteFacade;
import ru.faulab.javaee.design.patterns.sample.project.note.web.rest.vo.NoteData;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Locale;

@Path("/notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Tag(name = "Note Module")
public class NotesResource {
    @Inject
    private NoteFacade noteFacade;

    @GET
    @Operation(summary = "Get all notes", description = "Get list of notes")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Note.class))))
    public List<Note> getAllNotes() {
        return noteFacade.getAllNotes().toJavaList();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Get note by id", description = "Get note by id")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Note.class)))
    @ApiResponse(responseCode = "404", description = "Note not Found", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    public Note getNote(@Parameter(name = "id", required = true, example = "42") @PathParam("id") String id) {
        Note note = noteFacade.findById(id);
        if (note == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(String.format(Locale.ENGLISH, "'%1$s' not found", id)).type(MediaType.APPLICATION_JSON_TYPE).build());
        }
        return note;
    }

    @Path("{id}")
    @DELETE
    @Operation(summary = "Remove note by id", description = "Remove note by Id")
    public void deleteNote(@Parameter(name = "id", required = true, example = "42") @PathParam("id") String id) {
        noteFacade.deleteNote(id);
    }

    @POST
    @Operation(summary = "Create note", description = "Create note with content")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Note.class)))
    @ApiResponse(responseCode = "400", description = "Wrong note content format")
    public Note createNote(@RequestBody(description = "Parameters for create note", required = true) @Valid NoteData noteData) {
        return noteFacade.createNote(noteData.getContent());
    }

    @Path("{id}")
    @PUT
    @Operation(summary = "Update note", description = "Update note by id with content")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Note.class)))
    @ApiResponse(responseCode = "400", description = "Wrong note content format")
    @ApiResponse(responseCode = "404", description = "Note not Found", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    public Note updateNote(@Parameter(name = "id", required = true, example = "42") @PathParam("id") String id,
                           @RequestBody(description = "Parameters for update note", required = true) @Valid NoteData noteData) {
        Note updatedNote = noteFacade.updateNote(id, noteData.getContent());
        if (updatedNote == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(String.format(Locale.ENGLISH, "'%1$s' not found", id)).type(MediaType.APPLICATION_JSON_TYPE).build());
        }
        return updatedNote;
    }
}
/*
todo Add VO
{
  "exception": null,
  "fieldViolations": [],
  "propertyViolations": [],
  "classViolations": [],
  "parameterViolations": [
    {
      "constraintType": "PARAMETER",
      "path": "createNote.arg0.content",
      "message": "должно быть задано",
      "value": ""
    }
  ],
  "returnValueViolations": []
}
* */