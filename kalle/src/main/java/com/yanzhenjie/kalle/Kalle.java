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
package com.yanzhenjie.kalle;

import android.util.Log;

import com.yanzhenjie.kalle.download.BodyDownload;
import com.yanzhenjie.kalle.download.UrlDownload;
import com.yanzhenjie.kalle.simple.SimpleBodyRequest;
import com.yanzhenjie.kalle.simple.SimpleUrlRequest;

public final class Kalle {

    private static KalleConfig sConfig;

    private Kalle() {
    }

    public static KalleConfig getConfig() {
        setConfig(null);
        return sConfig;
    }

    public static void setConfig(KalleConfig config) {
        if (sConfig == null) {
            synchronized (KalleConfig.class) {
                if (sConfig == null)
                    sConfig = config == null ? KalleConfig.newBuilder().build() : config;
                else Log.w("Kalle", new IllegalStateException("Only allowed to configure once."));
            }
        }
    }

    public static SimpleUrlRequest.Api get(String url) {
        return SimpleUrlRequest.newApi(Url.newBuilder(url).build(), RequestMethod.GET);
    }

    public static SimpleUrlRequest.Api get(Url url) {
        return SimpleUrlRequest.newApi(url, RequestMethod.GET);
    }

    public static SimpleUrlRequest.Api head(String url) {
        return SimpleUrlRequest.newApi(Url.newBuilder(url).build(), RequestMethod.HEAD);
    }

    public static SimpleUrlRequest.Api head(Url url) {
        return SimpleUrlRequest.newApi(url, RequestMethod.HEAD);
    }

    public static SimpleUrlRequest.Api options(String url) {
        return SimpleUrlRequest.newApi(Url.newBuilder(url).build(), RequestMethod.OPTIONS);
    }

    public static SimpleUrlRequest.Api options(Url url) {
        return SimpleUrlRequest.newApi(url, RequestMethod.OPTIONS);
    }

    public static SimpleUrlRequest.Api trace(String url) {
        return SimpleUrlRequest.newApi(Url.newBuilder(url).build(), RequestMethod.TRACE);
    }

    public static SimpleUrlRequest.Api trace(Url url) {
        return SimpleUrlRequest.newApi(url, RequestMethod.TRACE);
    }

    public static SimpleBodyRequest.Api post(String url) {
        return SimpleBodyRequest.newApi(Url.newBuilder(url).build(), RequestMethod.POST);
    }

    public static SimpleBodyRequest.Api post(Url url) {
        return SimpleBodyRequest.newApi(url, RequestMethod.POST);
    }

    public static SimpleBodyRequest.Api put(String url) {
        return SimpleBodyRequest.newApi(Url.newBuilder(url).build(), RequestMethod.PUT);
    }

    public static SimpleBodyRequest.Api put(Url url) {
        return SimpleBodyRequest.newApi(url, RequestMethod.PUT);
    }

    public static SimpleBodyRequest.Api patch(String url) {
        return SimpleBodyRequest.newApi(Url.newBuilder(url).build(), RequestMethod.PATCH);
    }

    public static SimpleBodyRequest.Api patch(Url url) {
        return SimpleBodyRequest.newApi(url, RequestMethod.PATCH);
    }

    public static SimpleBodyRequest.Api delete(String url) {
        return SimpleBodyRequest.newApi(Url.newBuilder(url).build(), RequestMethod.DELETE);
    }

    public static SimpleBodyRequest.Api delete(Url url) {
        return SimpleBodyRequest.newApi(url, RequestMethod.DELETE);
    }

    public static class Download {

        public static UrlDownload.Api get(String url) {
            return UrlDownload.newApi(Url.newBuilder(url).build(), RequestMethod.GET);
        }

        public static UrlDownload.Api get(Url url) {
            return UrlDownload.newApi(url, RequestMethod.GET);
        }

        public static UrlDownload.Api head(String url) {
            return UrlDownload.newApi(Url.newBuilder(url).build(), RequestMethod.HEAD);
        }

        public static UrlDownload.Api head(Url url) {
            return UrlDownload.newApi(url, RequestMethod.HEAD);
        }

        public static UrlDownload.Api options(String url) {
            return UrlDownload.newApi(Url.newBuilder(url).build(), RequestMethod.OPTIONS);
        }

        public static UrlDownload.Api options(Url url) {
            return UrlDownload.newApi(url, RequestMethod.OPTIONS);
        }

        public static UrlDownload.Api trace(String url) {
            return UrlDownload.newApi(Url.newBuilder(url).build(), RequestMethod.TRACE);
        }

        public static UrlDownload.Api trace(Url url) {
            return UrlDownload.newApi(url, RequestMethod.TRACE);
        }

        public static BodyDownload.Api post(String url) {
            return BodyDownload.newApi(Url.newBuilder(url).build(), RequestMethod.POST);
        }

        public static BodyDownload.Api post(Url url) {
            return BodyDownload.newApi(url, RequestMethod.POST);
        }

        public static BodyDownload.Api put(String url) {
            return BodyDownload.newApi(Url.newBuilder(url).build(), RequestMethod.PUT);
        }

        public static BodyDownload.Api put(Url url) {
            return BodyDownload.newApi(url, RequestMethod.PUT);
        }

        public static BodyDownload.Api patch(String url) {
            return BodyDownload.newApi(Url.newBuilder(url).build(), RequestMethod.PATCH);
        }

        public static BodyDownload.Api patch(Url url) {
            return BodyDownload.newApi(url, RequestMethod.PATCH);
        }

        public static BodyDownload.Api delete(String url) {
            return BodyDownload.newApi(Url.newBuilder(url).build(), RequestMethod.DELETE);
        }

        public static BodyDownload.Api delete(Url url) {
            return BodyDownload.newApi(url, RequestMethod.DELETE);
        }
    }
}
