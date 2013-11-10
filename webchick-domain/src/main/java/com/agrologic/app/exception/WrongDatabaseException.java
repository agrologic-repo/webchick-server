package com.agrologic.app.exception;

/**
 * Throws while loading database in case when user and controller id does not exist in current database .
 *
 * @author Valery Manakhimov
 */
public class WrongDatabaseException extends Exception {
    private static final String USER_OR_FARM_CANNOT_FOUND = "The User ID and Farm ID could not be found. " +
            "\n Click on the OK button to open the Configuration window and manually enter them there.";
    public WrongDatabaseException() {
        super(USER_OR_FARM_CANNOT_FOUND);
    }
}
