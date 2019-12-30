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

import android.text.TextUtils;
import android.util.Log;

import com.yanzhenjie.kalle.Headers;
import com.yanzhenjie.kalle.Request;
import com.yanzhenjie.kalle.RequestBody;
import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.StringBody;
import com.yanzhenjie.kalle.UrlBody;
import com.yanzhenjie.kalle.connect.Interceptor;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Zhenjie Yan on 2018/3/26.
 */
public class LoggerInterceptor implements Interceptor {

    private final String mTag;
    private final boolean isEnable;

    public LoggerInterceptor(String tag, boolean isEnable) {
        this.mTag = tag;
        this.isEnable = isEnable;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (isEnable) {
            Response response = chain.proceed(request);

            String url = request.url().toString();

            StringBuilder log = new StringBuilder(String.format(" \nPrint Request: %1$s.", url));
            log.append(String.format("\nMethod: %1$s.", request.method().name()));

            Headers toHeaders = request.headers();
            for (Map.Entry<String, List<String>> entry : toHeaders.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                log.append(String.format("\n%1$s: %2$s.", key, TextUtils.join(";", values)));
            }

            if (request.method().allowBody()) {
                RequestBody body = request.body();
                if (body instanceof StringBody || body instanceof UrlBody) {
                    log.append(String.format(" \nRequest Body: %1$s.", body.toString()));
                }
            }

            log.append(String.format(" \nPrint Response: %1$s.", url));
            log.append(String.format(Locale.getDefault(), "\nCode: %1$d", response.code()));

            Headers fromHeaders = response.headers();
            for (Map.Entry<String, List<String>> entry : fromHeaders.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                log.append(String.format("\n%1$s: %2$s.", key, TextUtils.join(";", values)));
            }
            Log.i(mTag, log.toString());
            return response;
        }
        return chain.proceed(request);
    }

}