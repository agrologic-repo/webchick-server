package com.agrologic.app.network;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Valery on 15/07/14.
 */
public class MockExampleTest {

    /**
     * This example creates a mock iterator and makes it return “Hello” the first time method next() is called.
     * Calls after that return “World”. Then we can run normal assertions.
     */
    @Test
    public void iteratorWillReturnHelloWorld() {
        //arrange
        Iterator i = mock(Iterator.class);
        when(i.next()).thenReturn("Hello").thenReturn("World");
        //act
        String result = i.next() + " " + i.next();
        //assert
        assertEquals("Hello World", result);
    }

    /**
     * Stubs can also return different values depending on arguments passed into the method .
     * This creates a stub Comparable object and returns 1 if it is compared to a particular
     * String value (“Test” in this case)
     */
    @Test
    public void withArguments() {
        Comparable c = mock(Comparable.class);
        when(c.compareTo("Test")).thenReturn(1);
        assertEquals(1, c.compareTo("Test"));
    }

    /**
     * If the method has arguments but you really don’t care what gets passed or cannot predict it, use anyInt()
     * (and alternative values for other types)
     * This stub comparable returns -1 regardless of the actual method argument.
     * With void methods, this gets a bit tricky as you can’t use them in the when() call
     */
    @Test
    public void withUnspecifiedArguments() {
        Comparable c = mock(Comparable.class);
        when(c.compareTo(anyInt())).thenReturn(-1);
        assertEquals(-1, c.compareTo(5));
    }

    /**
     * The alternative syntax is doReturn(result).when(mock_object).void_method_call();
     * Instead of returning, you can also use .thenThrow() or doThrow() for void methods
     * This example throws an IOException when the mock OutputStream close method is called.
     * We verify easily that the OutputStreamWriter rethrows the exception of the wrapped output stream .
     */
    @Test(expected = IOException.class)
    public void OutputStreamWriter_rethrows_an_exception_from_OutputStream() throws IOException {
        OutputStream mock = mock(OutputStream.class);
        OutputStreamWriter osw = new OutputStreamWriter(mock);
        doThrow(new IOException()).when(mock).close();
        osw.close();
    }

    /**
     * To verify actual calls to underlying objects (typical mock object usage),
     * we can use verify(mock_object).method_call
     */
    @Test
    public void OutputStreamWriterClosesOutputStreamOnClose() throws IOException {
        OutputStream mock = mock(OutputStream.class);
        OutputStreamWriter osw = new OutputStreamWriter(mock);
        osw.close();
        verify(mock).close();
    }
}
