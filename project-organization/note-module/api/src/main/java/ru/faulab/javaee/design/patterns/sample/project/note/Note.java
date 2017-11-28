package ru.faulab.javaee.design.patterns.sample.project.note;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Dto
@JsonDeserialize(builder = NoteBuilder.class)
public interface Note extends Serializable {
    @NotNull
    @Nonnull
    @Size(min = 1)
    String getId();

    @NotNull
    @Nonnull
    @Size(min = 1, max = 256)
    String getContent();

    @NotNull
    @Nonnull
    LocalDateTime getCreatedWhen();

    Note withContent(@NotNull @Size(min = 1, max = 256) String value);

    @Nonnull
    static NoteBuilder builder() {
        return new NoteBuilder();
    }
}
