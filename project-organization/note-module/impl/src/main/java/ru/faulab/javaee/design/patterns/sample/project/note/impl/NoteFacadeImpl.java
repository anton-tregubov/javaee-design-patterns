package ru.faulab.javaee.design.patterns.sample.project.note.impl;

import io.vavr.collection.SortedSet;
import io.vavr.collection.TreeSet;
import io.vavr.control.Option;
import ru.faulab.javaee.design.patterns.sample.project.note.*;

import javax.annotation.*;
import javax.annotation.concurrent.ThreadSafe;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.time.LocalDateTime;
import java.util.*;

@ApplicationScoped
@Default
@ThreadSafe
public class NoteFacadeImpl implements NoteFacade {
    private static final Comparator<Note> COMPARATOR = Comparator.comparing(Note::getCreatedWhen).thenComparing(Note::getId);

    private SortedSet<Note> notes;

    @PostConstruct
    @PreDestroy
    void init() {
        notes = TreeSet.empty(COMPARATOR);
    }


    @Nonnull
    @Override
    public Iterable<Note> getAllNotes() {
        return notes.toJavaList();
    }

    @Nonnull
    @Override
    public Note createNote(String text) {
        Note note = Note.builder().id(UUID.randomUUID().toString()).content(text).createdWhen(LocalDateTime.now()).build();
        notes = notes.add(note);
        return note;
    }

    @Nullable
    @Override
    public Note findById(String id) {
        return getNoteById(id).getOrElse((Note) null);
    }

    @Override
    public void deleteNote(String id) {
        notes = getNoteById(id).map(notes::remove).getOrElse(notes);
    }

    @Override
    public Note updateNote(Note note) {
        Option<Note> noteById = getNoteById(note.getId());
        Note toAdd = noteById.map(n -> n.withContent(note.getContent())).getOrElseThrow(() -> new IllegalArgumentException(String.format(Locale.ENGLISH, "'%1$s' not found", note.getId())));
        notes = noteById.map(notes::remove).getOrElse(notes).add(toAdd);
        return toAdd;
    }

    private Option<Note> getNoteById(String id) {
        return notes.find(n -> Objects.equals(n.getId(), id));
    }
}
