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

package com.yanzhenjie.kalle.util;

import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Zhenjie Yan on 2019-06-24.
 */
public class LengthOutputStream extends OutputStream {

    private final AtomicLong mLength = new AtomicLong(0L);

    public LengthOutputStream() {
    }

    public long getLength() {
        return mLength.get();
    }

    public void write(long multiByte) {
        mLength.addAndGet(multiByte);
    }

    @Override
    public void write(int oneByte) {
        mLength.addAndGet(1);
    }

    @Override
    public void write(byte[] buffer) {
        mLength.addAndGet(buffer.length);
    }

    @Override
    public void write(byte[] buffer, int offset, int count) {
        mLength.addAndGet(count);
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }
}