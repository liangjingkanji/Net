/*
 * Copyright © 2018 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.urlconnect;

import android.text.TextUtils;

import com.yanzhenjie.kalle.connect.Connection;
import com.yanzhenjie.kalle.connect.stream.NullStream;
import com.yanzhenjie.kalle.connect.stream.SourceStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * <p>
 * Implement the network layer based on HttpURLConnection.
 * </p>
 * Created by Zhenjie Yan on 2017/2/12.
 */
public class URLConnection implements Connection {

    private HttpURLConnection mConnection;

    public URLConnection(HttpURLConnection connection) {
        this.mConnection = connection;
    }

    private static InputStream getInputStream(String contentEncoding, InputStream stream) throws IOException {
        if (!TextUtils.isEmpty(contentEncoding) && contentEncoding.contains("gzip")) {
            stream = new GZIPInputStream(stream);
        }
        return stream;
    }

    private static boolean hasBody(String method, int code) {
        return !"HEAD".equalsIgnoreCase(method) && hasBody(code);
    }

    private static boolean hasBody(int code) {
        return code > 100 && code != 204 && code != 205 && !(code >= 300 && code < 400);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return mConnection.getOutputStream();
    }

    @Override
    public int getCode() throws IOException {
        return mConnection.getResponseCode();
    }

    @Override
    public Map<String, List<String>> getHeaders() throws IOException {
        return mConnection.getHeaderFields();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        int code = mConnection.getResponseCode();
        if (!hasBody(mConnection.getRequestMethod(), code)) return new NullStream(this);
        if (code >= 400) {
            return getInputStream(mConnection.getContentEncoding(), new SourceStream(this, mConnection.getErrorStream()));
        }
        return getInputStream(mConnection.getContentEncoding(), new SourceStream(this, mConnection.getInputStream()));
    }

    @Override
    public void disconnect() {
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }

    @Override
    public void close() throws IOException {
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }
}