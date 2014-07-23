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
package com.thesett.junit.extensions;

import java.io.PrintStream;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.runner.Version;
import junit.textui.ResultPrinter;
import junit.textui.TestRunner;

import org.apache.log4j.Logger;

/**
 * The {@link junit.textui.TestRunner} does not provide very good error handling. It does not wrap exceptions and does
 * not print out stack traces, losing valuable error tracing information. This class overrides methods in it in order to
 * improve their error handling. The {@link TKTestRunner} is then built on top of this.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class TestRunnerImprovedErrorHandling extends TestRunner
{
    /** Used for logging. */
    /*final Logger log = Logger.getLogger(TestRunnerImprovedErrorHandling.class);*/

    /** Delegates to the super constructor. */
    public TestRunnerImprovedErrorHandling()
    {
        super();
    }

    /**
     * Delegates to the super constructor.
     *
     * @param printStream The location to write test results to.
     */
    public TestRunnerImprovedErrorHandling(PrintStream printStream)
    {
        super(printStream);
    }

    /**
     * Delegates to the super constructor.
     *
     * @param resultPrinter The location to write test results to.
     */
    public TestRunnerImprovedErrorHandling(ResultPrinter resultPrinter)
    {
        super(resultPrinter);
    }

    /**
     * Starts a test run. Analyzes the command line arguments and runs the given test suite.
     *
     * @param  args The command line arguments.
     *
     * @return The test results.
     *
     * @throws Exception Any exceptions falling through the tests are wrapped in Exception and rethrown.
     */
    protected TestResult start(String[] args) throws Exception
    {
        String testCase = "";
        boolean wait = false;

        for (int i = 0; i < args.length; i++)
        {
            if ("-wait".equals(args[i]))
            {
                wait = true;
            }
            else if ("-c".equals(args[i]))
            {
                testCase = extractClassName(args[++i]);
            }
            else if ("-v".equals(args[i]))
            {
                System.err.println("JUnit " + Version.id() + " by Kent Beck and Erich Gamma");
            }
            else
            {
                testCase = args[i];
            }
        }

        if ("".equals(testCase))
        {
            throw new Exception("Usage: TestRunner [-wait] testCaseName, where name is the name of the TestCase class");
        }

        try
        {
            Test suite = getTest(testCase);

            return doRun(suite, wait);
        }
        catch (Exception e)
        {
            /*log.warn("Got exception whilst creating and running test suite.", e);*/
            throw new Exception("Could not create and run the test suite.", e);
        }
    }
}
