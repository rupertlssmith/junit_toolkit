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
package com.thesett.junit.extensions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.extensions.TestDecorator;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * WrappedSuiteTestDecorator is a test decorator that wraps a test suite, or another wrapped suite, but provides the
 * same functionality for the {@link junit.extensions.TestDecorator#countTestCases()} and {@link TestSuite#testAt(int)}
 * methods as the underlying suite. It returns the values that these methods provide, to enable classes using decorated
 * tests to drill down to the underlying tests in the suite. That is to say that it indexes and reports the number of
 * distinct tests in the suite, not the number of test runs that would result from, for example, wrapping the suite in a
 * repeating decorator.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities
 * <tr><td> Provide access to the underlying tests in a suite.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class WrappedSuiteTestDecorator extends TestDecorator
{
    /** Used for logging. */
    /*private static final Logger log = Logger.getLogger(WrappedSuiteTestDecorator.class);*/

    /** Holds the test suite that this supplies access to. */
    protected final Test suite;

    /**
     * Creates a wrappred suite test decorator from a test suite.
     *
     * @param suite The test suite.
     */
    public WrappedSuiteTestDecorator(TestSuite suite)
    {
        super(suite);
        this.suite = suite;
    }

    /**
     * Creates a wrapped suite test decorator from another one.
     *
     * @param suite The test suite.
     */
    public WrappedSuiteTestDecorator(WrappedSuiteTestDecorator suite)
    {
        super(suite);
        this.suite = suite;
    }

    /**
     * Returns the test count of the wrapped suite.
     *
     * @return The test count of the wrapped suite.
     */
    public int countTestCases()
    {
        return suite.countTestCases();
    }

    /**
     * Gets the ith test from the test suite.
     *
     * @param  i The index of the test within the suite to get.
     *
     * @return The test with the specified index.
     */
    public Test testAt(int i)
    {
        /*log.debug("public Test testAt(int i = " + i + "): called");*/

        if (suite instanceof WrappedSuiteTestDecorator)
        {
            return ((WrappedSuiteTestDecorator) suite).testAt(i);
        }
        else if (suite instanceof TestSuite)
        {
            return ((TestSuite) suite).testAt(i);
        }

        // This should never happen.
        return null;
    }

    /**
     * Gets all the tests from the underlying test suite.
     *
     * @return All the tests from the underlying test suite.
     */
    public Iterable<Test> getAllUnderlyingTests()
    {
        /*log.debug("public Collection<Test> getAllUnderlyingTests(): called");*/

        Collection<Test> tests = new ArrayList<Test>();

        int numTests = countTestCases();
        /*log.debug("numTests = " + numTests);*/

        for (int i = 0; i < numTests; i++)
        {
            tests.add(testAt(i));
        }

        return tests;
    }
}
