package com.agrologic.app.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class ProgramInstanceLocker {

    /**  */
    private static FileChannel channel;

    /**  */
    private static File file;

    /**  */
    private static FileLock lock;

    /**
     * Create and lock file .
     *
     * @throws RuntimeException
     */
    public static void lockFile() {
        try {
            file = new File("RingOnRequest.lock");

            // Check if the lock exist
            if (file.exists()) {

                // if exist try to delete it
                file.delete();
            }

            // Try to get the lock
            channel = new RandomAccessFile(file, "rw").getChannel();
            lock = channel.tryLock();

            if (lock == null) {

                // File is lock by other application
                channel.close();

                throw new RuntimeException("Only 1 instance of program can run.");
            }

            // Add shutdown hook to release lock when application shutdown
            ShutdownHook shutdownHook = new ShutdownHook();

            Runtime.getRuntime().addShutdownHook(shutdownHook);
        } catch (IOException e) {
            throw new RuntimeException("Could not start process.", e);
        }
    }

    /**
     * Release and delete file lock.
     */
    public static void unlockFile() {

        // release and delete file lock
        try {
            if (lock != null) {
                lock.release();
                channel.close();
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ShutdownHook extends Thread {
        @Override
        public void run() {
            unlockFile();
        }
    }
}



