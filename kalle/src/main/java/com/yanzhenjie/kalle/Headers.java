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

import android.os.Build;
import android.text.TextUtils;

import com.yanzhenjie.kalle.util.ListMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;

/**
 * <p>
 * Http header.
 * </p>
 * Created in Jan 10, 2016 2:29:42 PM.
 */
public class Headers extends ListMap<String, String> {

    public static final String TIME_FORMAT_HTTP = "EEE, dd MMM y HH:mm:ss 'GMT'";
    public static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");

    public static final String KEY_ACCEPT = "Accept";
    public static final String VALUE_ACCEPT_ALL = "*/*";
    public static final String KEY_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String VALUE_ACCEPT_ENCODING = "gzip, deflate";
    public static final String KEY_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String VALUE_ACCEPT_LANGUAGE = getLanguage();
    public static final String KEY_ACCEPT_RANGE = "Accept-Range";
    public static final String KEY_COOKIE = "Cookie";
    public static final String KEY_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String KEY_CONTENT_ENCODING = "Content-Encoding";
    public static final String KEY_CONTENT_LENGTH = "Content-Length";
    public static final String KEY_CONTENT_RANGE = "Content-Range";
    public static final String KEY_CONTENT_TYPE = "Content-Type";
    public static final String VALUE_APPLICATION_URLENCODED = "application/x-www-form-urlencoded";
    public static final String VALUE_APPLICATION_FORM = "multipart/form-data";
    public static final String VALUE_APPLICATION_STREAM = "application/octet-stream";
    public static final String VALUE_APPLICATION_JSON = "application/json";
    public static final String VALUE_APPLICATION_XML = "application/xml";
    public static final String KEY_CACHE_CONTROL = "Cache-Control";
    public static final String KEY_CONNECTION = "Connection";
    public static final String VALUE_KEEP_ALIVE = "keep-alive";
    public static final String VALUE_CLOSE = "close";
    public static final String KEY_DATE = "Date";
    public static final String KEY_EXPIRES = "Expires";
    public static final String KEY_E_TAG = "ETag";
    public static final String KEY_HOST = "Host";
    public static final String KEY_IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String KEY_IF_NONE_MATCH = "If-None-Match";
    public static final String KEY_LAST_MODIFIED = "Last-Modified";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_RANGE = "Range";
    public static final String KEY_SET_COOKIE = "Set-Cookie";
    public static final String KEY_USER_AGENT = "User-Agent";
    public static final String VALUE_USER_AGENT = getUserAgent();

    public Headers() {
        super(new TreeMap<String, List<String>>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        }));
    }

    /**
     * Format to Hump-shaped words.
     */
    public static String formatKey(String key) {
        if (TextUtils.isEmpty(key)) return null;

        key = key.toLowerCase(Locale.ENGLISH);
        String[] words = key.split("-");

        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            String first = word.substring(0, 1);
            String end = word.substring(1);
            builder.append(first).append(end).append("-");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.lastIndexOf("-"));
        }
        return builder.toString();
    }

    /**
     * From the json format String parsing out the {@code Map<String, List<String>>} data.
     */
    public static Headers fromJSONString(String jsonString) throws JSONException {
        Headers headers = new Headers();
        JSONObject jsonObject = new JSONObject(jsonString);
        Iterator<String> keySet = jsonObject.keys();
        while (keySet.hasNext()) {
            String key = keySet.next();
            String value = jsonObject.optString(key);
            JSONArray values = new JSONArray(value);
            for (int i = 0; i < values.length(); i++) {
                headers.add(key, values.optString(i));
            }
        }
        return headers;
    }

    /**
     * Into a json format string.
     */
    public static String toJSONString(Headers headers) {
        JSONObject jsonObject = new JSONObject();
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            JSONArray value = new JSONArray(values);
            try {
                jsonObject.put(key, value);
            } catch (JSONException ignored) {
            }
        }
        return jsonObject.toString();
    }

    /**
     * Into a single key-value map.
     */
    public static Map<String, String> getRequestHeaders(Headers headers) {
        Map<String, String> headerMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            String trueValue = TextUtils.join("; ", value);
            headerMap.put(key, trueValue);
        }
        return headerMap;
    }

    /**
     * All the cookies in header information.
     */
    public static List<HttpCookie> getHttpCookieList(Headers headers) {
        List<HttpCookie> cookies = new ArrayList<>();
        for (String key : headers.keySet()) {
            if (key.equalsIgnoreCase(KEY_SET_COOKIE)) {
                List<String> cookieValues = headers.get(key);
                for (String cookieStr : cookieValues) {
                    cookies.addAll(HttpCookie.parse(cookieStr));
                }
            }
        }
        return cookies;
    }

    /**
     * A value of the header information.
     *
     * @param content      like {@code text/html;charset=utf-8}.
     * @param key          like {@code charset}.
     * @param defaultValue list {@code utf-8}.
     * @return If you have a value key, you will return the parsed value if you don't return the default value.
     */
    public static String parseSubValue(String content, String key, String defaultValue) {
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(key)) {
            StringTokenizer stringTokenizer = new StringTokenizer(content, ";");
            while (stringTokenizer.hasMoreElements()) {
                String valuePair = stringTokenizer.nextToken();
                int index = valuePair.indexOf('=');
                if (index > 0) {
                    String name = valuePair.substring(0, index).trim();
                    if (key.equalsIgnoreCase(name)) {
                        defaultValue = valuePair.substring(index + 1).trim();
                        break;
                    }
                }
            }
        }
        return defaultValue;
    }

    /**
     * Parse the time in GMT format to milliseconds.
     */
    public static long formatGMTToMillis(String gmtTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT_HTTP, Locale.US);
        formatter.setTimeZone(TIME_ZONE_GMT);
        Date date = formatter.parse(gmtTime);
        return date.getTime();
    }

    /**
     * Parse the time in milliseconds to GMT format.
     */
    public static String formatMillisToGMT(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT_HTTP, Locale.US);
        simpleDateFormat.setTimeZone(TIME_ZONE_GMT);
        return simpleDateFormat.format(date);
    }

    /**
     * Analysis the headers of the cache is valid time.
     *
     * @param headers http headers header.
     * @return Time corresponding milliseconds.
     */
    public static long analysisCacheExpires(Headers headers) {
        final long now = System.currentTimeMillis();

        long maxAge = 0;
        long staleWhileRevalidate = 0;

        String cacheControl = headers.getCacheControl();
        if (!TextUtils.isEmpty(cacheControl)) {
            StringTokenizer tokens = new StringTokenizer(cacheControl, ",");
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken().trim().toLowerCase(Locale.getDefault());
                if ((token.equals("no-cache") || token.equals("no-store"))) {
                    return 0;
                } else if (token.startsWith("max-age=")) {
                    maxAge = Long.parseLong(token.substring(8)) * 1000L;
                } else if (token.startsWith("must-revalidate")) {
                    // If must-revalidate, It must be from the server to validate expired.
                    return 0;
                } else if (token.startsWith("stale-while-revalidate=")) {
                    staleWhileRevalidate = Long.parseLong(token.substring(23)) * 1000L;
                }
            }
        }

        long localExpire = now;// Local expires time of cache.

        // Have CacheControl.
        if (!TextUtils.isEmpty(cacheControl)) {
            localExpire += maxAge;
            if (staleWhileRevalidate > 0) {
                localExpire += staleWhileRevalidate;
            }

            return localExpire;
        }

        final long expires = headers.getExpires();
        final long date = headers.getDate();

        // If the server through control the cache Expires.
        if (expires > date) {
            return now + expires - date;
        }
        return 0;
    }

    /**
     * Get language.
     */
    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        StringBuilder builder = new StringBuilder(language);
        if (!TextUtils.isEmpty(country))
            builder.append('-').append(country).append(',').append(language);
        return builder.toString();
    }

    /**
     * Get User-Agent.
     */
    public static String getUserAgent() {
        String webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/534.30 (KHTML, like Gecko) Version/5.0 %sSafari/534.30";

        StringBuffer buffer = new StringBuffer();
        buffer.append(Build.VERSION.RELEASE).append("; ");

        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        if (TextUtils.isEmpty(language)) {
            buffer.append(language.toLowerCase(locale));
            final String country = locale.getCountry();
            if (!TextUtils.isEmpty(country)) {
                buffer.append("-");
                buffer.append(country.toLowerCase(locale));
            }
        } else {
            buffer.append("en");
        }
        if ("REL".equals(Build.VERSION.CODENAME)) {
            if (Build.MODEL.length() > 0) {
                buffer.append("; ");
                buffer.append(Build.MODEL);
            }
        }
        if (Build.ID.length() > 0) {
            buffer.append(" Api/");
            buffer.append(Build.ID);
        }
        return String.format(webUserAgent, buffer, "Mobile ");
    }

    @Override
    public void add(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value))
            super.add(formatKey(key), value);
    }

    @Override
    public void set(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value))
            super.set(formatKey(key), value);
    }

    @Override
    public void add(String key, List<String> values) {
        if (!TextUtils.isEmpty(key) && !values.isEmpty())
            super.add(formatKey(key), values);
    }

    @Override
    public void set(String key, List<String> values) {
        if (!TextUtils.isEmpty(key) && !values.isEmpty())
            super.set(formatKey(key), values);
    }

    public void add(Headers headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            for (String value : values) {
                add(key, value);
            }
        }
    }

    @Override
    public List<String> remove(String key) {
        return super.remove(formatKey(key));
    }

    @Override
    public List<String> get(String key) {
        return super.get(formatKey(key));
    }

    @Override
    public String getFirst(String key) {
        return super.getFirst(formatKey(key));
    }

    @Override
    public boolean containsKey(String key) {
        return super.containsKey(formatKey(key));
    }

    /**
     * Replace all.
     */
    public void set(Headers headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@value #KEY_CACHE_CONTROL}.
     *
     * @return CacheControl.
     */
    public String getCacheControl() {
        List<String> cacheControls = get(KEY_CACHE_CONTROL);
        if (cacheControls == null) cacheControls = Collections.emptyList();
        return TextUtils.join(",", cacheControls);
    }

    /**
     * {@value KEY_CONTENT_DISPOSITION}.
     *
     * @return {@value KEY_CONTENT_DISPOSITION}.
     */
    public String getContentDisposition() {
        return getFirst(KEY_CONTENT_DISPOSITION);
    }

    /**
     * {@value #KEY_CONTENT_ENCODING}.
     *
     * @return ContentEncoding.
     */
    public String getContentEncoding() {
        return getFirst(KEY_CONTENT_ENCODING);
    }

    /**
     * {@value #KEY_CONTENT_LENGTH}.
     *
     * @return ContentLength.
     */
    public long getContentLength() {
        String contentLength = getFirst(KEY_CONTENT_LENGTH);
        if (TextUtils.isEmpty(contentLength)) contentLength = "0";
        return Long.parseLong(contentLength);
    }

    /**
     * {@value #KEY_CONTENT_TYPE}.
     *
     * @return ContentType.
     */
    public String getContentType() {
        return getFirst(KEY_CONTENT_TYPE);
    }

    /**
     * {@value #KEY_CONTENT_RANGE}.
     *
     * @return ContentRange.
     */
    public String getContentRange() {
        return getFirst(KEY_CONTENT_RANGE);
    }

    /**
     * {@value #KEY_DATE}.
     *
     * @return Date.
     */
    public long getDate() {
        return getDateField(KEY_DATE);
    }

    /**
     * {@value #KEY_E_TAG}.
     *
     * @return ETag.
     */
    public String getETag() {
        return getFirst(KEY_E_TAG);
    }

    /**
     * {@value #KEY_EXPIRES}.
     *
     * @return Expiration.
     */
    public long getExpires() {
        return getDateField(KEY_EXPIRES);
    }

    /**
     * {@value #KEY_LAST_MODIFIED}.
     *
     * @return LastModified.
     */
    public long getLastModified() {
        return getDateField(KEY_LAST_MODIFIED);
    }

    /**
     * {@value #KEY_LOCATION}.
     *
     * @return Location.
     */
    public String getLocation() {
        return getFirst(KEY_LOCATION);
    }

    /**
     * <p>
     * Returns the date value in milliseconds since 1970.1.1, 00:00h corresponding to the header field field. The
     * defaultValue will be returned if no such field can be found in the headers header.
     * </p>
     *
     * @param key the header field name.
     * @return the header field represented in milliseconds since January 1, 1970 GMT.
     */
    private long getDateField(String key) {
        String value = getFirst(key);
        if (!TextUtils.isEmpty(value))
            try {
                return formatGMTToMillis(value);
            } catch (ParseException ignored) {
            }
        return 0;
    }
}
