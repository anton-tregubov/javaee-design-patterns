package ru.faulab.javaee.design.patterns.sample.project.note;

import org.immutables.value.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value.Style(implementationNestedInBuilder = true,
        visibility = Value.Style.ImplementationVisibility.PACKAGE,
        builderVisibility = Value.Style.BuilderVisibility.PUBLIC,
        optionalAcceptNullable = true,
        get = {"get*", "is*"})
public interface Note {
    @NotNull
    @Size(min = 1)
    String getId();

    @NotNull
    @Size(min = 1, max = 256)
    String getContent();

    @NotNull
    LocalDateTime getCreatedWhen();
}
