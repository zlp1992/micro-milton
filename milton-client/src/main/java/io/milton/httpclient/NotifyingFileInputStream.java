/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.milton.httpclient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class NotifyingFileInputStream extends InputStream {

    private final InputStream fin;
    private final InputStream wrapped;
    private final ProgressListener listener;
    private final String fileName;
    private long pos;
    private final Long totalLength;
    // the system time we last notified the progress listener
    private long timeLastNotify;
    private long bytesSinceLastNotify;

    public NotifyingFileInputStream(File f, ProgressListener listener) throws FileNotFoundException, IOException {
        this.fin = FileUtils.openInputStream(f);
        this.wrapped = new BufferedInputStream(fin);
        this.listener = listener;
        this.totalLength = f.length();
        this.fileName = f.getAbsolutePath();
        this.timeLastNotify = System.currentTimeMillis();
    }

    /**
     *
     * @param in - the input stream containing file data
     * @param length - maybe null if unknown
     * @param path
     * @param listener
     * @throws IOException
     */
    public NotifyingFileInputStream(InputStream in, Long length, String path, ProgressListener listener) throws IOException {
        this.fin = in;
        this.wrapped = new BufferedInputStream(fin);
        this.listener = listener;
        this.totalLength = length;
        this.fileName = path;
        this.timeLastNotify = System.currentTimeMillis();
    }

    @Override
    public int read() throws IOException {
        increment(1);
        return wrapped.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        increment(b.length);
        return wrapped.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        increment(len);
        return wrapped.read(b, off, len);
    }

    private void increment(int len) {
        pos += len;
        notifyListener(len);
    }

    void notifyListener(int numBytes) {
        if (listener == null) {
            return;
        }
        listener.onRead(numBytes);
        bytesSinceLastNotify += numBytes;
        if (bytesSinceLastNotify < 1000) {
            //                log.trace( "notifyListener: not enough bytes: " + bytesSinceLastNotify);
            return;
        }
        int timeDiff = (int) (System.currentTimeMillis() - timeLastNotify);
        if (timeDiff > 10) {
            timeLastNotify = System.currentTimeMillis();
            listener.onProgress(pos, totalLength, fileName);
            bytesSinceLastNotify = 0;
        }
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(wrapped);
        IOUtils.closeQuietly(fin);
        super.close();
    }
}
