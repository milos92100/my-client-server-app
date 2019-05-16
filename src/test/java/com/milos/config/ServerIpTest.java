package com.milos.config;

import junit.framework.TestCase;

public class ServerIpTest extends TestCase {

    private String[] invalidIps = {"127.0.0", "256.0.0.1", "192.168.0.-1", "", null};
    private String[] validIps = {"192.168.1.10", "127.0.0.1"};

    public ServerIpTest(String testName) {
        super(testName);
    }

    public void testThatConstructorThrowsExceptionForInvalidIp() {
        for (String ip : invalidIps) {
            try {
                new ServerIp(ip);
                fail("IllegalArgumentException should be thrown for " + ip);
            } catch (Exception e) {
                assertEquals(IllegalArgumentException.class, e.getClass());
            }
        }
    }

    public void testThatInstanceIsCreatedForValidIp() {
        for (String ip : validIps) {
            ServerIp serverIp = new ServerIp(ip);
            assertEquals(ip, serverIp.getValue());
        }
    }
}
