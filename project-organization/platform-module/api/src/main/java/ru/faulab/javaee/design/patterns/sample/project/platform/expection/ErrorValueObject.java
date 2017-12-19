package ru.faulab.javaee.design.patterns.sample.project.platform.expection;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.faulab.javaee.design.patterns.sample.project.platform.expection.generated.ErrorValueObjectBuilder;
import ru.faulab.javaee.design.patterns.sample.project.platform.meta.DataTransferObject;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@DataTransferObject
@JsonDeserialize(builder = ErrorValueObjectBuilder.class)
@Schema(name = "Error", description = "Error description")
public interface ErrorValueObject extends Serializable {
    @NotNull
    @Schema(description = "Error identifier")
    int getErrorCode();

    @NotNull
    @Size(min = 1)
    @Schema(description = "User readable error description")
    String getUserMessage();/*localization OoS*/

    @Size(min = 1)
    @Schema(description = "Developer readable error description")
    @Nullable
    String getDeveloperMessage();

    static ErrorValueObjectBuilder builder() {return new ErrorValueObjectBuilder();}
}
