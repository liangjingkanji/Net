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
package com.yanzhenjie.kalle.simple;

import android.text.TextUtils;

import com.yanzhenjie.kalle.Headers;
import com.yanzhenjie.kalle.ResponseBody;
import com.yanzhenjie.kalle.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Zhenjie Yan on 2018/2/22.
 */
public class ByteArrayBody implements ResponseBody {

    private String mContentType;
    private byte[] mData;

    public ByteArrayBody(String contentType, byte[] data) {
        this.mContentType = contentType;
        this.mData = data;
    }

    @Override
    public String string() throws IOException {
        String charset = Headers.parseSubValue(mContentType, "charset", null);
        return TextUtils.isEmpty(charset) ? IOUtils.toString(mData) : IOUtils.toString(mData, charset);
    }

    @Override
    public byte[] byteArray() throws IOException {
        return mData;
    }

    @Override
    public InputStream stream() throws IOException {
        return new ByteArrayInputStream(mData);
    }

    @Override
    public void close() throws IOException {
        mData = null;
    }
}