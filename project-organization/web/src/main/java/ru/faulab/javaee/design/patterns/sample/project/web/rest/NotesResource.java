package ru.faulab.javaee.design.patterns.sample.project.web.rest;

import ru.faulab.javaee.design.patterns.sample.project.web.dto.NoteDto;

import javax.ws.rs.Path;
import java.util.*;

@Path("notes")
public class NotesResource
{
    @Path("/")
    public List<NoteDto> getAllNotes()
    {
        return Collections.emptyList();
    }
}
