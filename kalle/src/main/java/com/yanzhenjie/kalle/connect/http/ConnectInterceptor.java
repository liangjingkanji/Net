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
package com.yanzhenjie.kalle.connect.http;

import com.yanzhenjie.kalle.Headers;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.Request;
import com.yanzhenjie.kalle.RequestBody;
import com.yanzhenjie.kalle.RequestMethod;
import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.ResponseBody;
import com.yanzhenjie.kalle.connect.ConnectFactory;
import com.yanzhenjie.kalle.connect.Connection;
import com.yanzhenjie.kalle.connect.Interceptor;
import com.yanzhenjie.kalle.connect.Network;
import com.yanzhenjie.kalle.connect.StreamBody;
import com.yanzhenjie.kalle.cookie.CookieManager;
import com.yanzhenjie.kalle.exception.ConnectException;
import com.yanzhenjie.kalle.exception.ConnectTimeoutError;
import com.yanzhenjie.kalle.exception.HostError;
import com.yanzhenjie.kalle.exception.NetException;
import com.yanzhenjie.kalle.exception.NetworkError;
import com.yanzhenjie.kalle.exception.ReadException;
import com.yanzhenjie.kalle.exception.ReadTimeoutError;
import com.yanzhenjie.kalle.exception.URLError;
import com.yanzhenjie.kalle.exception.WriteException;
import com.yanzhenjie.kalle.util.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;

import static com.yanzhenjie.kalle.Headers.KEY_CONTENT_LENGTH;
import static com.yanzhenjie.kalle.Headers.KEY_CONTENT_TYPE;
import static com.yanzhenjie.kalle.Headers.KEY_COOKIE;
import static com.yanzhenjie.kalle.Headers.KEY_HOST;
import static com.yanzhenjie.kalle.Headers.KEY_SET_COOKIE;

/**
 * Created by Zhenjie Yan on 2018/2/20.
 */
class ConnectInterceptor implements Interceptor {

    private final CookieManager mCookieManager;
    private final ConnectFactory mFactory;
    private final Network mNetwork;

    private Connection mConnection;
    private boolean isCanceled;

    ConnectInterceptor() {
        this.mCookieManager = new CookieManager(Kalle.getConfig().getCookieStore());
        this.mFactory = Kalle.getConfig().getConnectFactory();
        this.mNetwork = Kalle.getConfig().getNetwork();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (isCanceled) throw new CancellationException("The request has been cancelled.");

        Request request = chain.request();
        RequestMethod method = request.method();

        if (method.allowBody()) {
            Headers headers = request.headers();
            RequestBody body = request.body();
            headers.set(KEY_CONTENT_LENGTH, Long.toString(body.contentLength()));
            headers.set(KEY_CONTENT_TYPE, body.contentType());
            mConnection = connect(request);
            writeBody(request);
        } else {
            mConnection = connect(request);
        }
        return readResponse(request);
    }

    /**
     * Cancel the request.
     */
    public void cancel() {
        isCanceled = true;
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }

    /**
     * Connect to the server to change the connection anomalies occurred.
     *
     * @param request target request.
     * @return connection between client and server.
     * @throws ConnectException anomalies that occurred during the connection.
     */
    private Connection connect(Request request) throws NetException {
        if (!mNetwork.isAvailable())
            throw new NetworkError(request, "Network Unavailable: ");

        try {
            Headers headers = request.headers();
            URI uri = new URI(request.url().toString());
            List<String> cookieHeader = mCookieManager.get(uri);
            if (cookieHeader != null && !cookieHeader.isEmpty())
                headers.add(KEY_COOKIE, cookieHeader);
            headers.set(KEY_HOST, uri.getHost());
            return mFactory.connect(request);
        } catch (URISyntaxException e) {
            throw new URLError(request, "The url syntax error: ", e);
        } catch (MalformedURLException e) {
            throw new URLError(request, "The url is malformed: ", e);
        } catch (UnknownHostException e) {
            throw new HostError(request, "Hostname can not be resolved: ", e);
        } catch (SocketTimeoutException e) {
            throw new ConnectTimeoutError(request, "Connect time out: ", e);
        } catch (Exception e) {
            throw new ConnectException(request, "An unknown exception: ", e);
        }
    }

    private void writeBody(Request request) throws WriteException {
        try {
            OutputStream stream = mConnection.getOutputStream();
            request.body().writeTo(IOUtils.toBufferedOutputStream(stream));
            IOUtils.closeQuietly(stream);
        } catch (Exception e) {
            throw new WriteException(request, null, e);
        }
    }

    private Response readResponse(Request request) throws NetException {
        try {
            int code = mConnection.getCode();
            Headers headers = parseResponseHeaders(mConnection.getHeaders());

            List<String> cookieList = headers.get(KEY_SET_COOKIE);
            if (cookieList != null && !cookieList.isEmpty())
                mCookieManager.add(URI.create(request.url().toString()), cookieList);

            String contentType = headers.getContentType();
            ResponseBody body = new StreamBody(contentType, mConnection.getInputStream());
            return Response.newBuilder().code(code).headers(headers).body(body).build();
        } catch (SocketTimeoutException e) {
            throw new ReadTimeoutError(request, "Read data time out: ", e);
        } catch (Exception e) {
            throw new ReadException(request, null, e);
        }
    }

    private Headers parseResponseHeaders(Map<String, List<String>> headersMap) {
        Headers headers = new Headers();
        for (Map.Entry<String, List<String>> entry : headersMap.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }
        return headers;
    }
}