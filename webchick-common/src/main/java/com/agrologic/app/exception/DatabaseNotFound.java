package com.agrologic.app.exception;

/**
 * Throws while loading database in case when database ot exist in program directory .
 *
 * @author Valery Manakhimov
 */
public class DatabaseNotFound extends GeneralException {
    private static final String FATAL_ERROR_DATABASE_NOT_FOUND = "Fatal error: Database  'agrodb' not found";

    public DatabaseNotFound() {
        super(FATAL_ERROR_DATABASE_NOT_FOUND);
    }

    public DatabaseNotFound(String message) {
        super(message);
    }
}
