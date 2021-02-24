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
package com.yanzhenjie.kalle.urlconnect;

import android.os.Build;

import com.yanzhenjie.kalle.Headers;
import com.yanzhenjie.kalle.Request;
import com.yanzhenjie.kalle.RequestMethod;
import com.yanzhenjie.kalle.connect.ConnectFactory;
import com.yanzhenjie.kalle.connect.Connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import static com.yanzhenjie.kalle.Headers.KEY_CONNECTION;
import static com.yanzhenjie.kalle.Headers.VALUE_CLOSE;

/**
 * <p>
 * Network connection actuator based on URLConnection.
 * </p>
 * Created by Zhenjie Yan on 2016/10/15.
 */
public class URLConnectionFactory implements ConnectFactory {

    private URLConnectionFactory(Builder builder) {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Connection connect(Request request) throws IOException {
        HttpURLConnection connection;
        URL url = new URL(request.url().toString(false));

        Proxy proxy = request.proxy();
        if (proxy == null)
            connection = (HttpURLConnection) url.openConnection();
        else
            connection = (HttpURLConnection) url.openConnection(proxy);

        connection.setConnectTimeout(request.connectTimeout());
        connection.setReadTimeout(request.readTimeout());
        connection.setInstanceFollowRedirects(false);

        if (connection instanceof HttpsURLConnection) {
            SSLSocketFactory sslSocketFactory = request.sslSocketFactory();
            if (sslSocketFactory != null)
                ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
            HostnameVerifier hostnameVerifier = request.hostnameVerifier();
            if (hostnameVerifier != null)
                ((HttpsURLConnection) connection).setHostnameVerifier(hostnameVerifier);
        }

        RequestMethod method = request.method();
        connection.setRequestMethod(method.toString());
        connection.setDoInput(true);
        boolean isAllowBody = isAllowBody(method);
        connection.setDoOutput(isAllowBody);

        Headers headers = request.headers();

        if (isAllowBody) {
            long contentLength = headers.getContentLength();
            if (contentLength <= Integer.MAX_VALUE)
                connection.setFixedLengthStreamingMode((int) contentLength);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                connection.setFixedLengthStreamingMode(contentLength);
            else connection.setChunkedStreamingMode(256 * 1024);
        }

        List<String> values = headers.get(KEY_CONNECTION);
        headers.set(KEY_CONNECTION, Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT ? values.get(0) : VALUE_CLOSE);
        Map<String, String> requestHeaders = Headers.getRequestHeaders(headers);
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        connection.connect();
        return new URLConnection(connection);
    }

    private boolean isAllowBody(RequestMethod method) {
        boolean allowRequestBody = method.allowBody();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return allowRequestBody && method != RequestMethod.DELETE;
        return allowRequestBody;
    }

    public static class Builder {
        private Builder() {
        }

        public URLConnectionFactory build() {
            return new URLConnectionFactory(this);
        }
    }
}
