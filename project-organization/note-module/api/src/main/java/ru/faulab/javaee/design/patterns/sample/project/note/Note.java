package ru.faulab.javaee.design.patterns.sample.project.note;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Value.Immutable
@Value.Style(implementationNestedInBuilder = true,
        jdkOnly = true,
        visibility = Value.Style.ImplementationVisibility.PUBLIC,
        builderVisibility = Value.Style.BuilderVisibility.PUBLIC,
        optionalAcceptNullable = true,
        get = {"get*", "is*"})
@JsonSerialize(as = NoteBuilder.ImmutableNote.class)
@JsonDeserialize(as = NoteBuilder.ImmutableNote.class)
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
