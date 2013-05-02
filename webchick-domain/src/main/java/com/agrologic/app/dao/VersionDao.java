package com.agrologic.app.dao;

public interface VersionDao {

    /**
     * Retrieve the database installed version;
     *
     * @return version the of installed database
     */
    String getVersion();
}
