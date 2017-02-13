package com.noseplugapp.android;

/**
 * FatalException is an exception thrown when we cannot recover and the app must crash.
 * This should only be used for "impossible" situations.
 */
public final class FatalException extends RuntimeException {
    public FatalException(String message) {
        super(message);
    }

    public FatalException(String message, Throwable cause) {
        super(message, cause);
    }

    public FatalException(Throwable cause) {
        super(cause);
    }
}
