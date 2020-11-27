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

import com.yanzhenjie.kalle.Canceller;
import com.yanzhenjie.kalle.Headers;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.Request;
import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.exception.NetException;
import com.yanzhenjie.kalle.exception.NoCacheError;
import com.yanzhenjie.kalle.exception.ParseError;
import com.yanzhenjie.kalle.recorder.LogRecorder;
import com.yanzhenjie.kalle.simple.cache.Cache;
import com.yanzhenjie.kalle.simple.cache.CacheMode;
import com.yanzhenjie.kalle.simple.cache.CacheStore;
import com.yanzhenjie.kalle.util.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;

import static com.yanzhenjie.kalle.Headers.KEY_IF_MODIFIED_SINCE;
import static com.yanzhenjie.kalle.Headers.KEY_IF_NONE_MATCH;

abstract class BasicWorker<T extends SimpleRequest, S> implements Callable<S>, Canceller {

    private static final long MAX_EXPIRES = System.currentTimeMillis() + 100L * 365L * 24L * 60L * 60L * 1000L;

    private final T mRequest;
    private final CacheStore mCacheStore;
    private final Converter mConverter;
    private final Type mSucceed;

    BasicWorker(T request, Type succeed) {
        this.mRequest = request;
        this.mSucceed = succeed;
        this.mCacheStore = Kalle.getConfig().getCacheStore();
        this.mConverter = request.converter() == null ? Kalle.getConfig().getConverter() : request.converter();
    }

    @Override
    public final S call() throws Exception {
        Response response = tryReadCacheBefore();
        if (response != null) return buildSimpleResponse(response, true);

        tryAttachCache();
        try {
            Request request = mRequest.request();
            LogRecorder.INSTANCE.recordRequest(request.logId(), request.location(), request.method().toString(), request.headers().toMap(), request.getLog());

            response = requestNetwork(mRequest);

            int code = response.code();
            if (code == 304) {
                Response cacheResponse = tryReadCacheAfter(-1);
                if (cacheResponse != null) {
                    return buildSimpleResponse(cacheResponse, true);
                } else {
                    return buildSimpleResponse(response, false);
                }
            }
            Headers headers = response.headers();

            byte[] body = {};
            if (code != 204) {
                body = response.body().byteArray();
            }
            IOUtils.closeQuietly(response);
            tryDetachCache(code, headers, body);
            response = buildResponse(code, headers, body);
            return buildSimpleResponse(response, false);
        } catch (IOException e) {
            Response cacheResponse = tryReadCacheAfter(-1);
            if (cacheResponse != null) {
                return buildSimpleResponse(cacheResponse, true);
            }
            Request request = mRequest.request();

            String errorMsg = null;
            int code = 0;
            if (response != null) {
                errorMsg = response.getLog();
                code = response.code();
            }
            LogRecorder.INSTANCE.recordException(request.logId(), request.getTimeMillis(), code, e.getLocalizedMessage(), errorMsg);
            throw e;
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    /**
     * Perform a network request.
     *
     * @param request target request.
     * @return {@link Response}.
     * @throws IOException when connecting to the network, write data, read the data {@link IOException} occurred.
     */
    protected abstract Response requestNetwork(T request) throws IOException;

    /**
     * Cancel request.
     */
    public abstract void cancel();

    private Response tryReadCacheBefore() throws NoCacheError {
        CacheMode cacheMode = mRequest.cacheMode();
        switch (cacheMode) {
            case HTTP: {
                Cache cache = mCacheStore.get(mRequest.cacheKey());
                if (cache == null) return null;
                if (cache.getExpires() > System.currentTimeMillis()) {
                    return buildResponse(cache.getCode(), cache.getHeaders(), cache.getBody());
                }
                break;
            }
            case HTTP_YES_THEN_WRITE_CACHE:
            case NETWORK:
            case NETWORK_YES_THEN_HTTP:
            case NETWORK_YES_THEN_WRITE_CACHE:
            case NETWORK_NO_THEN_READ_CACHE: {
                // Nothing.
                return null;
            }
            case READ_CACHE: {
                Cache cache = mCacheStore.get(mRequest.cacheKey());
                if (cache != null) {
                    return buildResponse(cache.getCode(), cache.getHeaders(), cache.getBody());
                }
                throw new NoCacheError(mRequest.request(), "No cache found");
            }
            case READ_CACHE_NO_THEN_NETWORK:
            case READ_CACHE_NO_THEN_HTTP: {
                Cache cache = mCacheStore.get(mRequest.cacheKey());
                if (cache != null) {
                    return buildResponse(cache.getCode(), cache.getHeaders(), cache.getBody());
                }
                break;
            }
            case READ_CACHE_NO_THEN_NETWORK_THEN_WRITE_CACHE: {
                Cache cache = mCacheStore.get(mRequest.cacheKey());
                if (cache != null) {
                    return buildResponse(cache.getCode(), cache.getHeaders(), cache.getBody());
                }
                break;
            }
        }
        return null;
    }

    private void tryAttachCache() {
        CacheMode cacheMode = mRequest.cacheMode();
        switch (cacheMode) {
            case HTTP:
            case HTTP_YES_THEN_WRITE_CACHE: {
                Cache cacheEntity = mCacheStore.get(mRequest.cacheKey());
                if (cacheEntity != null) attachCache(cacheEntity.getHeaders());
                break;
            }
            case NETWORK:
            case NETWORK_YES_THEN_HTTP:
            case NETWORK_YES_THEN_WRITE_CACHE:
            case NETWORK_NO_THEN_READ_CACHE:
            case READ_CACHE:
            case READ_CACHE_NO_THEN_NETWORK:
            case READ_CACHE_NO_THEN_HTTP: {
                // Nothing.
                break;
            }
            case READ_CACHE_NO_THEN_NETWORK_THEN_WRITE_CACHE: {
                Cache cacheEntity = mCacheStore.get(mRequest.cacheKey());
                if (cacheEntity != null) attachCache(cacheEntity.getHeaders());
                break;
            }
        }
    }

    private void tryDetachCache(int code, Headers headers, byte[] body) {
        CacheMode cacheMode = mRequest.cacheMode();
        switch (cacheMode) {
            case HTTP: {
                long expires = Headers.analysisCacheExpires(headers);
                if (expires > 0 || headers.getLastModified() > 0) {
                    detachCache(code, headers, body, expires);
                }
                break;
            }
            case HTTP_YES_THEN_WRITE_CACHE: {
                detachCache(code, headers, body, MAX_EXPIRES);
                break;
            }
            case NETWORK: {
                break;
            }
            case NETWORK_YES_THEN_HTTP: {
                long expires = Headers.analysisCacheExpires(headers);
                if (expires > 0 || headers.getLastModified() > 0) {
                    detachCache(code, headers, body, expires);
                }
                break;
            }
            case NETWORK_YES_THEN_WRITE_CACHE: {
                detachCache(code, headers, body, MAX_EXPIRES);
                break;
            }
            case NETWORK_NO_THEN_READ_CACHE:
            case READ_CACHE:
            case READ_CACHE_NO_THEN_NETWORK: {
                break;
            }
            case READ_CACHE_NO_THEN_HTTP: {
                long expires = Headers.analysisCacheExpires(headers);
                if (expires > 0 || headers.getLastModified() > 0) {
                    detachCache(code, headers, body, expires);
                }
                break;
            }
            case READ_CACHE_NO_THEN_NETWORK_THEN_WRITE_CACHE: {
                //禁止服务器错误时存入缓存
                if (code != 400 && code != 403 && code != 404 && code != 500 && code != 501 && code != 502 && code != 503 && code != 504 && code != 505 && code != 506 && code != 507 && code != 509 && code != 510) {
                    detachCache(code, headers, body, MAX_EXPIRES);
                }
                break;
            }
        }
    }

    private Response tryReadCacheAfter(int code) {
        CacheMode cacheMode = mRequest.cacheMode();
        switch (cacheMode) {
            case HTTP:
            case HTTP_YES_THEN_WRITE_CACHE: {
                if (code == 304) {
                    Cache cache = mCacheStore.get(mRequest.cacheKey());
                    if (cache != null) {
                        return buildResponse(cache.getCode(), cache.getHeaders(), cache.getBody());
                    }
                }
                break;
            }
            case NETWORK:
            case NETWORK_YES_THEN_HTTP:
            case NETWORK_YES_THEN_WRITE_CACHE: {
                break;
            }
            case NETWORK_NO_THEN_READ_CACHE: {
                Cache cache = mCacheStore.get(mRequest.cacheKey());
                if (cache != null) {
                    return buildResponse(cache.getCode(), cache.getHeaders(), cache.getBody());
                }
                break;
            }
            case READ_CACHE:
            case READ_CACHE_NO_THEN_NETWORK: {
                break;
            }
            case READ_CACHE_NO_THEN_HTTP: {
                if (code == 304) {
                    Cache cache = mCacheStore.get(mRequest.cacheKey());
                    if (cache != null) {
                        return buildResponse(cache.getCode(), cache.getHeaders(), cache.getBody());
                    }
                }
                break;
            }
            case READ_CACHE_NO_THEN_NETWORK_THEN_WRITE_CACHE: {
                Cache cache = mCacheStore.get(mRequest.cacheKey());
                if (cache != null) {
                    return buildResponse(cache.getCode(), cache.getHeaders(), cache.getBody());
                }
                break;
            }
        }
        return null;
    }

    private void attachCache(Headers cacheHeaders) {
        Headers headers = mRequest.headers();
        String eTag = cacheHeaders.getETag();
        if (eTag != null) headers.set(KEY_IF_NONE_MATCH, eTag);

        long lastModified = cacheHeaders.getLastModified();
        if (lastModified > 0)
            headers.set(KEY_IF_MODIFIED_SINCE, Headers.formatMillisToGMT(lastModified));
    }

    private void detachCache(int code, Headers headers, byte[] body, long expires) {

        String cacheKey = mRequest.cacheKey();

        Cache entity = new Cache();
        entity.setKey(cacheKey);
        entity.setCode(code);
        entity.setHeaders(headers);
        entity.setBody(body);
        entity.setExpires(expires);
        mCacheStore.replace(cacheKey, entity);
    }

    private Response buildResponse(int code, Headers headers, byte[] body) {
        return Response.newBuilder()
                .code(code)
                .headers(headers)
                .body(new ByteArrayBody(headers.getContentType(), body))
                .build();
    }

    private S buildSimpleResponse(Response response, boolean cache) throws IOException {
        Request request = mRequest.request();
        try {
            S result = mConverter.convert(mSucceed, request, response, cache);
            LogRecorder.INSTANCE.recordResponse(request.logId(), request.getTimeMillis(), response.code(), response.headers().toMap(), response.getLog());
            return result;
        } catch (NetException e) {
            throw e;
        } catch (Exception e) {
            throw new ParseError(request, "An exception occurred while parsing the data", e);
        }
    }
}