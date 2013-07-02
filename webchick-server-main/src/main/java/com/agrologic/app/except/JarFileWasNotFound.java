package com.agrologic.app.except;

public class JarFileWasNotFound extends Exception {
    public String givenName;
    public static final String JAR_FILE_WAS_NOT_FOUND = "JAR file with given name was not found in classpath ";

    public JarFileWasNotFound() {
        this(JAR_FILE_WAS_NOT_FOUND);
    }

    public JarFileWasNotFound(String givenName) {
        super(JAR_FILE_WAS_NOT_FOUND);
        this.givenName = givenName;
    }

    @Override
    public String getMessage() {
        if (givenName != null) {
            return super.getMessage() + "\n" + givenName;
        }
        return super.getMessage();
    }
}
