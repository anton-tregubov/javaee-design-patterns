package ru.faulab.javaee.design.patterns.sample.project.note.impl;

import io.vavr.collection.SortedSet;
import io.vavr.collection.Traversable;
import io.vavr.collection.TreeSet;
import io.vavr.control.Option;
import ru.faulab.javaee.design.patterns.sample.project.note.Note;
import ru.faulab.javaee.design.patterns.sample.project.note.NoteFacade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.concurrent.ThreadSafe;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

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
    public Traversable<Note> getAllNotes() {
        return notes;
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


    @Nullable
    @Override
    public Note updateNote(String id, String content) {
        Option<Note> noteById = getNoteById(id);
        Note toAdd = noteById.map(n -> n.withContent(content)).getOrNull();
        if (toAdd == null) {
            return null;
        }
        notes = noteById.map(notes::remove).getOrElse(notes).add(toAdd);
        return toAdd;
    }

    private Option<Note> getNoteById(String id) {
        return notes.find(n -> Objects.equals(n.getId(), id));
    }
}
