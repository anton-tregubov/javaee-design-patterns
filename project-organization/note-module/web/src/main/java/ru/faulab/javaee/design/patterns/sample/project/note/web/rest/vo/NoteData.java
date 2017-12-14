package ru.faulab.javaee.design.patterns.sample.project.note.web.rest.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.faulab.javaee.design.patterns.sample.project.note.web.rest.vo.generated.NoteDataBuilder;
import ru.faulab.javaee.design.patterns.sample.project.platform.ValueObject;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ValueObject
@JsonDeserialize(builder = NoteDataBuilder.class)
@Schema(name = "Editable note", description = "Note data that can be changed")
public interface NoteData {
    @NotNull
    @Size(min = 1, max = 256)
    @Schema(required = true, description = "Note content", example = "some text data")
    String getContent();

    static NoteDataBuilder builder() {
        return new NoteDataBuilder();
    }
}
