package com.agrologic.app.util;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CommonUtil {
    public static void env() {
        Map<String, String> envs = System.getenv();
        for (String env : envs.keySet()) {
            String value = System.getenv(env);
            if (value != null) {
                System.out.format("%s=%s%n", env, value);
            } else {
                System.out.format("%s is not assigned.%n", env);
            }
        }
    }

    /**
     * Not recommended. Test should not sleepMiliSeconds, but should wait for some condition instead.
     *
     * @param milliseconds Time to milliseconds in milliseconds
     */
    public static void sleepMiliSeconds(long milliseconds) {

        try {
            Thread.sleep(TimeUnit.MICROSECONDS.toMillis(milliseconds));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Not recommended. Test should not sleepMiliSeconds, but should wait for some condition instead.
     *
     * @param milliseconds Time to milliseconds in seconds
     */
    public static void sleepSeconds(long milliseconds) {

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(milliseconds));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
