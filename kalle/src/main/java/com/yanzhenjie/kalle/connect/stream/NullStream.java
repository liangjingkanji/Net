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

import java.io.InputStream;

/**
 * Created by Zhenjie Yan on 2018/2/25.
 */
public class NullStream extends InputStream {

    private final Connection mConnection;

    public NullStream(Connection connection) {
        this.mConnection = connection;
    }

    @Override
    public int read() {
        return 0;
    }

    @Override
    public int read(byte[] b) {
        return 0;
    }

    @Override
    public int read(byte[] b, int off, int len) {
        return 0;
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(mConnection);
    }

    @Override
    public long skip(long n) {
        return 0;
    }

    @Override
    public int available() {
        return 0;
    }

    @Override
    public void reset() {
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public void mark(int limit) {
    }
}