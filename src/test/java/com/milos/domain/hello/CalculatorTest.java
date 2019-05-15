package com.milos.domain.hello;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CalculatorTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CalculatorTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(CalculatorTest.class);
    }

    public void testThatAddShouldReturnRightResultForTwoNumbers(){
        Calculator calculator = new Calculator();
        int expected = 5;
        int actual = calculator.add(2, 3);

        assertEquals(expected, actual);
    }
}
