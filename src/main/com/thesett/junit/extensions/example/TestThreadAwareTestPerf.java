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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.thesett.junit.extensions.AsymptoticTestCase;
import com.thesett.junit.extensions.TestThreadAware;

/**
 * TestThreadAwareTestPerf gives an example of usgin the thread aware interface to perform per thread setups and
 * teardowns for tests.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class TestThreadAwareTestPerf extends AsymptoticTestCase implements TestThreadAware
{
    /** Used for logging. */
    private static final Logger log = Logger.getLogger(TestThreadAwareTestPerf.class);

    /** Holds the fields set up on a per thread basis. */
    final ThreadLocal<TestThreadLocalSetup> perThreadSetup = new ThreadLocal<TestThreadLocalSetup>();

    /**
     * Creates a named thread aware test.
     *
     * @param name The test name.
     */
    public TestThreadAwareTestPerf(String name)
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
        TestSuite suite = new TestSuite("Test Thread Aware Example Tests");

        suite.addTest(new TestThreadAwareTestPerf("testPerThreadFixture"));

        return suite;
    }

    /**
     * Does nothing. Improve this example by having this use some thread locals to access per thread setups.
     *
     * @param i The size of the test.
     */
    public void testPerThreadFixture(int i)
    {
        log.debug("public void testPerThreadFixture(int i): called in thread " + Thread.currentThread().getName());
    }

    /** Called when a test thread is created. */
    public void threadSetUp()
    {
        log.debug("public void threadSetUp(): called in thread " + Thread.currentThread().getName());
        perThreadSetup.set(new TestThreadLocalSetup());
    }

    /** Called when a test thread is destroyed. */
    public void threadTearDown()
    {
        log.debug("public void threadTearDown(): called in thread " + Thread.currentThread().getName());
        perThreadSetup.remove();

    }

    /**
     * Used to hold per thread fields. Dummy class to illustrate the example.
     */
    private static class TestThreadLocalSetup
    {
    }
}
