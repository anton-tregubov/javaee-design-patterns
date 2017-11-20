package ru.faulab.javaee.design.patterns.sample.project.note.web.rest;

import ru.faulab.javaee.design.patterns.sample.project.note.web.dto.NoteDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("notes")
@Consumes(MediaType.APPLICATION_JSON)
public class NotesResource
{
    @Path("/")
    public List<NoteDto> getAllNotes()
    {
        return Collections.emptyList();
    }
}
