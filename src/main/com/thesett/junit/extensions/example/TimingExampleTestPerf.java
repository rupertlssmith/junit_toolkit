/*
 * Copyright Rupert Smith, 2005 to 2008.
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

import com.thesett.common.properties.ParsedProperties;
import com.thesett.junit.extensions.AsymptoticTestCase;
import com.thesett.junit.extensions.TestThreadAware;
import com.thesett.junit.extensions.util.TestContextProperties;
import com.thesett.junit.extensions.util.TestUtils;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

/**
 * TimingExampleTest implements some short pauses that test invocations can try out as dummy test material. The pause
 * time per test, or per setup method, or per thread setup can be configured by setting properties.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Pause a specified number of times.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class TimingExampleTestPerf extends AsymptoticTestCase implements TestThreadAware
{
    /** Used for logging. */
    private static final Logger log = Logger.getLogger(TimingExampleTestPerf.class);

    /** The test pause property name. */
    private static final String SLEEP_TIME_PROPNAME = "testPause";

    /** The default time to sleep in the test wait loop. */
    private static final long SLEEP_TIME_DEFAULT = 100;

    /** The setup pause property name. */
    private static final String SLEEP_TIME_SETUP_PROPNAME = "setupPause";

    /** The default time to sleep during the setup method. */
    private static final long SLEEP_TIME_SETUP_DEFAULT = 0;

    /** The per thread setup pause property name. */
    private static final String SLEEP_TIME_SETUP_THREAD_PROPNAME = "setupThreadPause";

    /** The default time to sleep during the setup per thread. */
    private static final long SLEEP_TIME_SETUP_THREAD_DEFAULT = 0;

    /** Used to read the tests configurable properties through. */
    final ParsedProperties testProps;

    /** Holds the test sleep time. */
    private long testSleepTime;

    /**
     * Creates a new example test case object.
     *
     * @param name The name of the test.
     */
    public TimingExampleTestPerf(String name)
    {
        super(name);

        ParsedProperties defaults = new ParsedProperties();

        /** Holds the time to sleep in the test wait loop. */
        defaults.setPropertyIfNull(SLEEP_TIME_PROPNAME, SLEEP_TIME_DEFAULT);

        /** Holds the time to sleep during the setup method. */
        defaults.setPropertyIfNull(SLEEP_TIME_SETUP_PROPNAME, SLEEP_TIME_SETUP_DEFAULT);

        /** Holds the time to sleep during the thread setup method. */
        defaults.setPropertyIfNull(SLEEP_TIME_SETUP_THREAD_PROPNAME, SLEEP_TIME_SETUP_THREAD_DEFAULT);

        testProps = TestContextProperties.getInstance(defaults);
    }

    /**
     * Compiles all the tests in this class into a suite.
     *
     * @return The test suite.
     */
    public static Test suite()
    {
        // Build a new test suite
        TestSuite suite = new TestSuite("Timing Example Tests");

        suite.addTest(new TimingExampleTestPerf("testShortWait"));
        // suite.addTest(new TimingExampleTestPerf("testVeryShortWait"));

        return suite;
    }

    /**
     * Does n short waits.
     *
     * @param n The number of waits.
     */
    public void testShortWait(int n)
    {
        log.debug("public void testShortWait(int " + n + "): called");

        long sleepTime = testSleepTime;

        for (int i = 0; i < n; i++)
        {
            try
            {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException e)
            {
                // Ignore, but restore the interrupted flag.
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Loops n times but does very little.
     *
     * @param n The number of waits.
     */
    public void testVeryShortWait(int n)
    {
        log.debug("public void testVeryShortWait(int " + n + ")");

        for (int i = 0; i < n; i++)
        {
            // Execute a few instructions, but nothing that takes very long...
            Math.pow(Math.E, i);
        }
    }

    /** Called when a test thread is created. */
    public void threadSetUp()
    {
        long threadSetupSleepTime = testProps.getPropertyAsLong(SLEEP_TIME_SETUP_THREAD_PROPNAME);

        TestUtils.pause(threadSetupSleepTime);
    }

    /** Called when a test thread is destroyed. */
    public void threadTearDown()
    {
    }

    /** Setup that can be configured to pause for a short time. */
    protected void setUp()
    {
        testSleepTime = testProps.getPropertyAsLong(SLEEP_TIME_PROPNAME);

        Long setupSleepTime = testProps.getPropertyAsLong(SLEEP_TIME_SETUP_PROPNAME);

        TestUtils.pause(setupSleepTime);
    }
}
