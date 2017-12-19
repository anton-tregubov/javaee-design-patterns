package ru.faulab.javaee.design.patterns.sample.project.platform.expection;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public abstract class SimpleProjectException extends RuntimeException {

    public SimpleProjectException(String message) {
        super(message);
    }

    public SimpleProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract ErrorValueObject toErrorValueObject();
}
