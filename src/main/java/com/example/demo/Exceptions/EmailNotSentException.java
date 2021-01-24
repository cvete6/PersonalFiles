package com.example.demo.Exceptions;

/**
 * Exception class when email is not sent.
 */
public class EmailNotSentException extends RuntimeException {
    /**
     * Exception method that returns a message when email is not sent.
     *
     * @param message that explains what kind of exception happened.
     */
    public EmailNotSentException(String message) {
        super(message);
    }

    /**
     * Exception method that returns message and the cause for the exception when email is not sent.
     *
     * @param message that explains what kind of exception happened.
     * @param cause   that explains why the exception happened.
     */
    public EmailNotSentException(String message, Throwable cause) {
        super(message, cause);
    }
}