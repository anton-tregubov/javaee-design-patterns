package ru.faulab.javaee.design.patterns.sample.project.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.oas.annotations.media.Schema;
import ru.faulab.javaee.design.patterns.sample.project.note.generated.NoteBuilder;
import ru.faulab.javaee.design.patterns.sample.project.platform.Dto;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Dto
@JsonDeserialize(builder = NoteBuilder.class)
@Schema(name = "Note", description = "Some information that need to be stored")
public interface Note extends Serializable {
    @NotNull
    @Nonnull
    @Size(min = 1)
    @Schema(required = true, description = "Locally unique identifier")
    String getId();

    @NotNull
    @Nonnull
    @Size(min = 1, max = 256)
    @Schema(required = true, description = "Note content")
    String getContent();

    @NotNull
    @Nonnull
    @Schema(required = true, description = "date when note was created")
    LocalDateTime getCreatedWhen();

    @JsonIgnore
    Note withContent(@NotNull @Size(min = 1, max = 256) String value);

    @Nonnull
    static NoteBuilder builder() {
        return new NoteBuilder();
    }
}
