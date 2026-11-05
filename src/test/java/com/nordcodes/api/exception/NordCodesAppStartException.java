package com.nordcodes.api.exception;

/**
 * Исключение, в случае траблов запуска приложения NordCodes.
 */
public class NordCodesAppStartException extends RuntimeException {
    public NordCodesAppStartException(Exception e) {
        super("Failed to start tested application", e);
    }

    public NordCodesAppStartException(String message, Exception cause) {
        super(message, cause);
    }

}
