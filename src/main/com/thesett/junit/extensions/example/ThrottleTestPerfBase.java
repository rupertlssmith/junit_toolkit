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

import com.thesett.common.throttle.Throttle;
import com.thesett.junit.extensions.AsymptoticTestCase;

/**
 * ThrottleTestPerfBase is a base implementation of tests for performance testing the accuracy of throttle
 * implementations.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Check one second accuracy of throttle by running for number of ops per second.
 *     <td> {@link Throttle}
 * </table></pre>
 *
 * @author Rupert Smith
 */
public abstract class ThrottleTestPerfBase extends AsymptoticTestCase
{
    /** Used to hold the test setup on a per thread basis. */
    final ThreadLocal<Throttle> threadSetup = new ThreadLocal<Throttle>();

    /**
     * Creates a named throttle test.
     *
     * @param name The name of the test.
     */
    public ThrottleTestPerfBase(String name)
    {
        super(name);
    }

    /**
     * Should be implemented to provide a throttle implementation to test.
     *
     * @return The throttle to test.
     */
    public abstract Throttle getTestThrottle();

    /**
     * Check one second accuracy of throttle by running for number of ops per second.
     *
     * @param opsPerSecond The test size, interpreted as ops per second.
     */
    public void testThrottleAccuracy(int opsPerSecond)
    {
        // Ensure the test throttle is setup on the current test thread.
        Throttle testThrottle = threadSetup.get();

        if (testThrottle == null)
        {
            testThrottle = getTestThrottle();
            threadSetup.set(testThrottle);
        }

        // Setup the throttle to the desired rate and reset it.
        testThrottle.setRate(opsPerSecond);

        // Loop for the specified operations per second, to get a one second test.
        // The first loop returns straight away, so one extra is done.
        for (int i = 0; i <= opsPerSecond; i++)
        {
            try
            {
                testThrottle.throttle();
            }
            catch (InterruptedException e)
            {
                // If the test is interrupted then it cannot be accurate, so report a failure in that case. Interruption
                // acknowledged so the exception can be ignored.
                e = null;
                Thread.currentThread().interrupt();
                fail("Test interrupted so throttle accuracy cannot be accurately measured.");
            }
        }
    }
}
