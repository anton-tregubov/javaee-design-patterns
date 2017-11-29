package ru.faulab.javaee.design.patterns.sample.project.platform;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.lang.annotation.*;

@JsonSerialize // Jackson automatic integration, why not?
@Value.Style(implementationNestedInBuilder = true,
        jdkOnly = true,
        visibility = Value.Style.ImplementationVisibility.PACKAGE,/*should be package but glassfish*/
        builderVisibility = Value.Style.BuilderVisibility.PUBLIC,
        optionalAcceptNullable = true,
        packageGenerated = "*.generated",
        get = {"get*", "is*"})
@Documented
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Dto {
}
