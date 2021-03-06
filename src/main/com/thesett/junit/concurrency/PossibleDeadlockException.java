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
package com.thesett.junit.concurrency;

/**
 * PossibleDeadlockException is used to signal that two test threads being executed by a {@link ThreadTestCoordinator}
 * may be in a state of deadlock because they are mutually blocking each other or one is waiting on the other and the
 * other has been blocked elsewhere for longer than a specified timeout.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Signal a possible state of deadlock between coordinated test threads.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class PossibleDeadlockException extends RuntimeException
{
    /**
     * Create a new possible deadlock execption.
     *
     * @param message The exception message.
     */
    public PossibleDeadlockException(String message)
    {
        super(message);
    }
}
