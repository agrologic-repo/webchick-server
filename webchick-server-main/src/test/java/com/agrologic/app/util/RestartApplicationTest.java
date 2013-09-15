package com.agrologic.app.util;

import com.agrologic.app.except.JarFileWasNotFound;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class RestartApplicationTest {

    @Test
    public void testResetUsingManagementFactoryAndRuntimeClass() throws Exception {

    }

    @Test
    public void testRestartUsingFileClassAndProcessBuilder() throws Exception {

    }

    @Test
    public void testRestartUsingFileClassAndProcessBuilderWithArgs() throws Exception {

    }

    @Test(expected = JarFileWasNotFound.class)
    @Ignore
    public void isGetJarFileThrowFileWasNotFoundException() throws Exception {
        String jarName = "webchick-server-test.jar";
        File expected = new File(jarName);
        RestartApplication.getJarFileByGivenJarName(jarName);
    }

    @Test
    @Ignore
    public void getJarFileWasFound() throws Exception {
        String jarName = "webchick-server.jar";
        File expected = new File(jarName);
        File actual = RestartApplication.getJarFileByGivenJarName(jarName);
        Assert.assertEquals(expected.getName(), actual.getName());
    }
}
