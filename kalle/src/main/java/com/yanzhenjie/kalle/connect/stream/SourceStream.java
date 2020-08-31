/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.connect.stream;

import com.yanzhenjie.kalle.connect.Connection;
import com.yanzhenjie.kalle.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Zhenjie Yan on 2018/2/25.
 */
public class SourceStream extends InputStream {

    private final Connection mConnection;
    private final InputStream mStream;

    public SourceStream(Connection connection, InputStream stream) {
        this.mConnection = connection;
        this.mStream = stream;
    }

    @Override
    public int read() throws IOException {
        return mStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return mStream.read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return mStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return mStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return mStream.available();
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(mStream);
        IOUtils.closeQuietly(mConnection);
    }

    @Override
    public void reset() throws IOException {
        mStream.reset();
    }

    @Override
    public synchronized void mark(int limit) {
        mStream.mark(limit);
    }

    @Override
    public boolean markSupported() {
        return mStream.markSupported();
    }
}