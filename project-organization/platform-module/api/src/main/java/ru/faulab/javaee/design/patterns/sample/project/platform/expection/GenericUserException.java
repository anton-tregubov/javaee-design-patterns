package ru.faulab.javaee.design.patterns.sample.project.platform.expection;

public class GenericUserException extends SimpleProjectException {
    private final int errorCode;

    public GenericUserException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorValueObject toErrorValueObject() {
        return ErrorValueObject.builder().errorCode(errorCode).userMessage(getLocalizedMessage()).build();
    }
}
