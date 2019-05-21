package com.milos.config;

import junit.framework.TestCase;

public class ServerPortTest extends TestCase {

    private int[] invalidPorts = {0, -1, -2};

    public ServerPortTest(String testName) {
        super(testName);
    }

    public void testThatConstructorThrowsIllegalArgumentExceptionForInvalidValue() {
        try {
            for (int given : invalidPorts) {
                new ServerPort(given);
                fail("IllegalArgumentException should be thrown for " + given);
            }
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    public void testGetValue() {
        int expectedValue = 3306;

        ServerPort port = new ServerPort(expectedValue);
        assertEquals(expectedValue, port.getValue());
    }
}
