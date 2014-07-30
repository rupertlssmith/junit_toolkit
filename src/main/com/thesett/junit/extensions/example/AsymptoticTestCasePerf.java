/*
 * Copyright The Sett Ltd, 2005 to 2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thesett.junit.extensions.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.thesett.junit.extensions.AsymptoticTestCase;
import com.thesett.junit.extensions.AsymptoticTestDecorator;

/**
 * AsymptoticTestCasePerf is a performance test to test and demonstrate the capabilities of the
 * {@link AsymptoticTestCase} class. It uses the data structures in the java.util package as material to run asymptotic
 * performance tests on. Technically these are really the classes that this test is testing and not the
 * AsymptoticTestCase class itself.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Test asymptotic performance of a TreeMap.
 * <tr><td> Test asymptotic performance of a HashMap.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class AsymptoticTestCasePerf extends AsymptoticTestCase
{
    /** Used for logging. */
    private static final Logger log = Logger.getLogger(AsymptoticTestDecorator.class);

    /**
     * Creates a new AsymptoticTestCasePerf object.
     *
     * @param name The name of the test.
     */
    public AsymptoticTestCasePerf(String name)
    {
        super(name);
    }

    /**
     * Compiles all the tests in this class into a suite.
     *
     * @return The test suite.
     */
    public static Test suite()
    {
        // Build a new test suite
        TestSuite suite = new TestSuite("AsymptoticTestCase Tests");

        // Add all the tests defined in this class (using the default constructor)
        // suite.addTest(new AsymptoticTestCasePerf("testHashMapMemory"));
        // suite.addTest(new AsymptoticTestCasePerf("testTreeMapMemory"));
        suite.addTest(new AsymptoticTestCasePerf("testHashMapContainsKeyPerformance"));
        suite.addTest(new AsymptoticTestCasePerf("testTreeMapContainsKeyPerformance"));

        return suite;
    }

    /**
     * Does nothing.
     *
     * @throws Exception Any exceptions raised by this set up method are allowed to fall through to the test runner.
     */
    public void setUp() throws Exception
    {
    }

    /**
     * Does nothing.
     *
     * @throws Exception Any exceptions raised by this clean up method are allowed to fall through to the test runner.
     */
    public void tearDown() throws Exception
    {
    }

    /**
     * Test asymptotic memory performance of a HashMap.
     *
     * @param  n The number of elements to insert into the hash map.
     *
     * @throws Exception Any exceptions are allowed to fall through.
     */
    public void testHashMapMemory(int n) throws Exception
    {
        Random random = new Random();

        Map testMap = new HashMap();

        for (int i = 0; i < n; i++)
        {
            int nextRandom = random.nextInt();
            testMap.put(Integer.toString(nextRandom), Integer.toString(nextRandom));
        }

        // Leave the map allocated with a member variable reference to it to ensure the memory gets counted.
    }

    /**
     * Test asymptotic memory performance of a TreeMap.
     *
     * @param n The number of elements to insert into the hash map.
     */
    public void testTreeMapMemory(int n)
    {
        Random random = new Random();

        Map testMap = new TreeMap();

        for (int i = 0; i < n; i++)
        {
            int nextRandom = random.nextInt();
            testMap.put(Integer.toString(nextRandom), Integer.toString(nextRandom));
        }

        // Leave the map allocated with a member variable reference to it to ensure the memory gets counted.
    }

    /**
     * Test asymptotic time performance of the containsKey operation on a HashMap. This works on a pre-built hash map of
     * ten thousand keys.
     *
     * @param  n The number of times to do a containsKey operation.
     *
     * @throws Exception Any exceptions are allowed to fall through.
     */
    public void testHashMapContainsKeyPerformance(int n) throws Exception
    {
        log.debug("public void testHashMapContainsKeyPerformance(int " + n + "): called");

        Random random = new Random();

        Map testMap = new HashMap();

        for (int i = 0; i < n; i++)
        {
            testMap.put(Integer.toString(i), Integer.toString(i));
        }

        for (int i = 0; i < 1000; i++)
        {
            testMap.containsKey(Integer.toString(random.nextInt()));
        }

        testMap = null;
    }

    /**
     * Test asymptotic time performance of the containsKey operation on a HashMap. This works on a pre-built tree map of
     * ten thousand keys.
     *
     * @param  n The number of times to do a containsKey operation.
     *
     * @throws Exception Any exceptions are allowed to fall through.
     */
    public void testTreeMapContainsKeyPerformance(int n) throws Exception
    {
        log.debug("public void testTreeMapContainsKeyPerformance(int " + n + ")");

        Random random = new Random();

        Map testMap = new TreeMap();

        for (int i = 0; i < n; i++)
        {
            testMap.put(Integer.toString(i), Integer.toString(i));
        }

        for (int i = 0; i < 1000; i++)
        {
            testMap.containsKey(Integer.toString(random.nextInt()));
        }

        testMap = null;
    }
}
