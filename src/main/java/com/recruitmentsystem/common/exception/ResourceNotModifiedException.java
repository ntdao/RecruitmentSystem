package com.recruitmentsystem.common.exception;

//@ResponseStatus(value = HttpStatus.NOT_MODIFIED)
public class ResourceNotModifiedException extends RuntimeException {
    public ResourceNotModifiedException(String message) {
        super(message);
    }
}
