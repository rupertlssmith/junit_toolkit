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
package com.thesett.junit.concurrency;

import java.util.concurrent.ThreadFactory;

/**
 * Implements a default thread factory.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Create default threads with no specialization.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class DefaultThreadFactory implements ThreadFactory
{
    /**
     * Constructs a new <tt>Thread</tt>.
     *
     * @param  r A runnable to be executed by new thread instance.
     *
     * @return The constructed thread.
     */
    public Thread newThread(Runnable r)
    {
        return new Thread(r);
    }
}
