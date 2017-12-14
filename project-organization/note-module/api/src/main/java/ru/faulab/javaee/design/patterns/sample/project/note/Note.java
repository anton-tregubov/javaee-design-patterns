package ru.faulab.javaee.design.patterns.sample.project.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.faulab.javaee.design.patterns.sample.project.note.generated.NoteBuilder;
import ru.faulab.javaee.design.patterns.sample.project.platform.DomainModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@DomainModel
@JsonDeserialize(builder = NoteBuilder.class)
@Schema(name = "Note", description = "Some information that need to be stored")
public interface Note extends Serializable {
    @NotNull
    @Size(min = 1)
    @Schema(required = true, description = "Locally unique identifier", example = "42")
    String getId();

    @NotNull
    @Size(min = 1, max = 256)
    @Schema(required = true, description = "Note content", example = "Some note content")
    String getContent();

    @NotNull
    @Schema(required = true, description = "date when note was created", example = "2017-12-15T01:27:39.258")
    LocalDateTime getCreatedWhen();

    @JsonIgnore
    Note withContent(@NotNull @Size(min = 1, max = 256) String value);

    static NoteBuilder builder() {
        return new NoteBuilder();
    }
}
