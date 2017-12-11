package ru.faulab.javaee.design.patterns.sample.project.note;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface NoteFacade {
    @Nonnull
    Iterable<Note> getAllNotes();

    @Nonnull
    Note createNote(String text);

    @Nullable
    Note findById(String id);

    void deleteNote(String id);

    @Nullable
    Note updateNote(String id, String content);
}
