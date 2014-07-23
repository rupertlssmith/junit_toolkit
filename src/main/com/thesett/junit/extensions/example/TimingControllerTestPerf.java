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

import com.thesett.common.throttle.SleepThrottle;
import com.thesett.common.throttle.Throttle;
import com.thesett.junit.extensions.AsymptoticTestCase;
import com.thesett.junit.extensions.TimingController;
import com.thesett.junit.extensions.TimingControllerAware;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the use of the timing controller interface to output additional test timings from a single test method. There
 * is also a test to ensure that when the timing controller is called from threads other than the one that called the
 * test method, it still functions correctly. This is a common use case when writing asynchronous tests.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Check timing controller can accept multiple timings.
 * <tr><td> Check timing controller can be called from other threads.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class TimingControllerTestPerf extends AsymptoticTestCase implements TimingControllerAware
{
    /** Defines the test rate, used to space out the test timings. */
    private static final float TEST_RATE = 100f;

    /** The timing controller. */
    private TimingController tc;

    /**
     * Creates a named timing controller test.
     *
     * @param name The test name.
     */
    public TimingControllerTestPerf(String name)
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

        suite.addTest(new TimingControllerTestPerf("testMultipleTimingsOtherThread"));

        return suite;
    }

    /**
     * Registers multiple test timings from the thread that called this method.
     *
     * @param num The number of timings to register.
     */
    public void testMultipleTimings(int num)
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
                // Restrict the test rate.
                throttle.throttle();

                // Only output multiple timings when the timing controller is available.
                if (tc != null)
                {
                    tc.completeTest(true, 1);
                }
            }
        }
        catch (InterruptedException e)
        {
            // If the test runner wants to stop right away it will raise InterruptedException. Terminate the
            // loop immediately. This may also happen if one of the throttle calls is interrupted. Interrupt
            // acknowledged so the exception may be ignored.
            e = null;
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Registers multiple test timings from a different thread that called this method. This checks that the test runner
     * is not confused by the call back to the timing controller coming from another thread, and demonstrates that
     * asynchronous testing is possible.
     *
     * @param num The number of timings to register.
     */
    public void testMultipleTimingsOtherThread(final int num)
    {
        // Get the timing controller if it has been made available.
        final TimingController tc = getTimingController().getControllerForCurrentThread();

        // Set up a throttle to space the test results out with.
        final Throttle throttle = new SleepThrottle();
        throttle.setRate(TEST_RATE);

        try
        {
            throttle.throttle();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }

        Runnable testRunnable =
            new Runnable()
            {
                public void run()
                {
                    for (int i = 0; i < num; i++)
                    {
                        // Restrict the test rate.
                        try
                        {
                            throttle.throttle();
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
                        }

                        // Only output multiple timings when the timing controller is available.
                        if (tc != null)
                        {
                            try
                            {
                                tc.completeTest(true, 1);
                                tc.completeTest(false, 1);
                            }

                            // If the test runner wants to stop right away it will raise InterruptedException. Terminate the
                            // loop immediately.
                            catch (InterruptedException e)
                            {
                                break;
                            }
                        }
                    }
                }
            };

        Thread testThread = new Thread(testRunnable);

        testThread.start();

        try
        {
            testThread.join();
        }
        catch (InterruptedException e)
        {
            // Ignore, but restore the interrupted flag.
            Thread.currentThread().interrupt();
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
     * Gets the timing controller passed into the
     * {@link #setTimingController(com.thesett.junit.extensions.TimingController)} method.
     *
     * @return The timing controller, or null if none has been set.
     */
    public TimingController getTimingController()
    {
        return tc;
    }
}
