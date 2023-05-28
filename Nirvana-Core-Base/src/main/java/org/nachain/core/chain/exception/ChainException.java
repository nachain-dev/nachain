package org.nachain.core.chain.exception;


public class ChainException extends RuntimeException {

    public ChainException(String message) {
        super(message);
    }

    public ChainException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
    }

    public ChainException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChainException(Throwable cause, String messageFormat, Object... args) {
        super(String.format(messageFormat, args), cause);
    }
}
