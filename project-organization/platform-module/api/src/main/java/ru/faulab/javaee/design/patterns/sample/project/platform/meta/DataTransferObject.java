package ru.faulab.javaee.design.patterns.sample.project.platform.meta;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.lang.annotation.*;

@JsonSerialize // Jackson automatic integration, why not?
@Value.Style(implementationNestedInBuilder = true,
        jdkOnly = true,
        visibility = Value.Style.ImplementationVisibility.PACKAGE,
        builderVisibility = Value.Style.BuilderVisibility.PUBLIC,
        optionalAcceptNullable = true,
        packageGenerated = "*.generated",
        get = {"get*", "is*"},
        validationMethod = Value.Style.ValidationMethod.VALIDATION_API)
@Documented
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface DataTransferObject {
}
