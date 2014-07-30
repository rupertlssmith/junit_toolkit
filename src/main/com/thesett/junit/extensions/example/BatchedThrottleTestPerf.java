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

import com.thesett.common.throttle.BatchedThrottle;
import com.thesett.common.throttle.Throttle;

/**
 * Provides a {@link BatchedThrottle} to test run with the accuracy tests in {@link ThrottleTestPerfBase}.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Run accuracy tests on BatchedThrottle <td> {@link BatchedThrottle}
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class BatchedThrottleTestPerf extends ThrottleTestPerfBase
{
    /**
     * Creates a new named sleep throttle test.
     *
     * @param name The test name.
     */
    public BatchedThrottleTestPerf(String name)
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
        TestSuite suite = new TestSuite("BatchedThrottle Tests");

        suite.addTest(new BatchedThrottleTestPerf("testThrottleAccuracy"));

        return suite;
    }

    /**
     * Generates the throttle for the abstract base class to test, in this case a {@link BatchedThrottle}.
     *
     * @return A sleep throttle to test.
     */
    public Throttle getTestThrottle()
    {
        return new BatchedThrottle();
    }
}
