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

import com.thesett.common.throttle.SleepThrottle;
import com.thesett.common.throttle.Throttle;
import com.thesett.junit.extensions.AsymptoticTestCase;
import com.thesett.junit.extensions.TimingController;
import com.thesett.junit.extensions.TimingControllerAware;

/**
 * Tests the use of the timing controller interface to output test timings from self timed tests.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Output self timed test timings.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class SelfTimedTestPerf extends AsymptoticTestCase implements TimingControllerAware
{
    /** Used for debugging. */
    private static final Logger log = Logger.getLogger(SelfTimedTestPerf.class);

    /** Defines the test rate, used to space out the test timings. */
    private static final float TEST_RATE = 100f;

    /** The timing controller. */
    private TimingController tc;

    /**
     * Creates a named timing controller test.
     *
     * @param name The test name.
     */
    public SelfTimedTestPerf(String name)
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
        TestSuite suite = new TestSuite("Timing Controller Example Tests");

        suite.addTest(new SelfTimedTestPerf("testSelfTimed"));

        return suite;
    }

    /**
     * Registers multiple test timings from the thread that called this method.
     *
     * @param num The number of timings to register.
     */
    public void testSelfTimed(int num)
    {
        // Get the timing controller if it has been made available.
        TimingController tc = getTimingController().getControllerForCurrentThread();

        // Set up a throttle to space the test results out with.
        Throttle throttle = new SleepThrottle();
        throttle.setRate(TEST_RATE);

        try
        {
            throttle.throttle();

            for (int i = 0; i < num; i++)
            {
                long start = System.nanoTime();

                // Restrict the test rate.
                throttle.throttle();

                long end = System.nanoTime();
                long timeTaken = end - start;

                // Only output multiple timings when the timing controller is available.
                if (tc != null)
                {
                    tc.completeTest(true, 1, timeTaken);
                }
            }
        }
        catch (InterruptedException e)
        {
            // If the test runner wants to stop right away it will raise InterruptedException. Terminate the
            // loop immediately. This may also happen if one of the throttle calls is interrupted by some external
            // request to shutdown.
            log.debug(
                "Got InterruptedException when using timing controller to register test result or calling the throttle.");
            Thread.currentThread().interrupt();

            // Exception noted and ignored.
            e = null;
        }
    }

    /**
     * Used by test runners that can supply a {@link com.thesett.junit.extensions.TimingController} to set the
     * controller on this aware test.
     *
     * @param controller The timing controller.
     */
    public void setTimingController(TimingController controller)
    {
        tc = controller;
    }

    /**
     * Gets the timing controller passed into the {@link #setTimingController(TimingController)} method.
     *
     * @return The timing controller, or null if none has been set.
     */
    public TimingController getTimingController()
    {
        return tc;
    }
}
