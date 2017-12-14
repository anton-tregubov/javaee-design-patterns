package ru.faulab.javaee.design.patterns.sample.project.note;

import io.vavr.collection.Traversable;

import javax.annotation.Nullable;

public interface NoteFacade {
    Traversable<Note> getAllNotes();

    Note createNote(String text);

    @Nullable
    Note findById(String id);

    void deleteNote(String id);

    @Nullable
    Note updateNote(String id, String content);
}
