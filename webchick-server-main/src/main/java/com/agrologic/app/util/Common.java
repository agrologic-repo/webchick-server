package com.agrologic.app.util;

import java.util.Map;

public class Common {
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
}
