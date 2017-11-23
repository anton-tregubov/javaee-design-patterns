package ru.faulab.javaee.design.patterns.sample.project.note.impl;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import ru.faulab.javaee.design.patterns.sample.project.note.Note;
import ru.faulab.javaee.design.patterns.sample.project.note.NoteFacade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.util.Objects;
import java.util.SortedSet;

@ApplicationScoped
@Default
public class NoteFacadeImpl implements NoteFacade {
    private final SortedSet<Note> notes;

    public NoteFacadeImpl() {
        notes = Sets.newTreeSet((n1, n2) -> ComparisonChain.start()
                .compare(n1.getCreatedWhen(), n2.getCreatedWhen())
                .compare(n1.getId(), n2.getId()).result());
    }

    @Nonnull
    @Override
    public Iterable<Note> getAllNotes() {
        return ImmutableSortedSet.copyOf(notes);
    }

    @Nonnull
    @Override
    public Note createNote(String text) {
        return null;
    }

    @Nullable
    @Override
    public Note findById(String id) {
        return notes.stream().filter(n -> Objects.equals(n.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public void deleteNote(String id) {
        notes.removeIf(n -> Objects.equals(n.getId(), id));
    }

    @Override
    public Note updateNote(Note note) {
        deleteNote(note.getId());
        return createNote(note.getContent());
    }
}
