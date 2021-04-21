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

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.URLUtil;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Add the mPath to the URL, such as:
 * <pre>
 * Url url = Url.newBuilder("http://www.example.com/xx")
 *      .scheme("https")
 *      .port(8080)
 *      .path("yy")
 *      .query("abc", "123")
 *      .setFragment("article")
 *      .build();
 * ...
 * The real url is: <code>https://www.example.com:8080/xx/yy?abc=123#article</code>.
 * </pre>
 * <pre>
 * Url url = Url.newBuilder("http://www.example.com/xx/yy?abc=123")
 *      .setPath("/aa/bb/cc")
 *      .setQuery("mln=456&ijk=789")
 *      .build();
 * ...
 * The real url is: <code>http://www.example.com/aa/bb/cc?mln=456&ijk=789</code>.
 * </pre>
 * <pre>
 * Url url = Url.newBuilder("http://www.example.com/user/photo/search?name=abc").build();
 * Url newUrl = url.location("../../get?name=mln");
 * ...
 * The new url is: <code>http://www.example.com/get?name=abc</code>.
 * </pre>
 * Created by Zhenjie Yan on 2018/2/9.
 */
public class Url {

    private final String mScheme;
    private final String mHost;
    private final int mPort;
    private final String mPath;
    private final String mQuery;
    private final String mFragment;
    private Url(Builder builder) {
        this.mScheme = builder.mScheme;
        this.mHost = builder.mHost;
        this.mPort = builder.mPort;
        this.mPath = builder.mPath;
        this.mQuery = builder.mQuery.build().toString(false);
        this.mFragment = builder.mFragment;
    }

    public static Builder newBuilder(String url) {
        return new Builder(url);
    }

    private static int convertPort(int port) {
        return port > 0 ? port : 80;
    }

    private static List<String> convertPath(String path) {
        List<String> pathList = new LinkedList<>();
        if (!TextUtils.isEmpty(path)) {
            while (path.startsWith("/")) path = path.substring(1);
            while (path.endsWith("/")) path = path.substring(0, path.length() - 1);
            String[] pathArray = path.split("/");
            Collections.addAll(pathList, pathArray);
        }
        return pathList;
    }

    private static Params convertQuery(String query) {
        Params.Builder params = Params.newBuilder();
        if (!TextUtils.isEmpty(query)) {
            if (query.startsWith("?")) query = query.substring(1);
            String[] paramArray = query.split("&");
            for (String param : paramArray) {
                String key;
                String value = "";
                int end;
                if ((end = param.indexOf("=")) > 0) {
                    key = param.substring(0, end);
                    if (end < param.length() - 1) {
                        value = param.substring(end + 1);
                    }
                } else {
                    key = param;
                }
                params.add(key, value);
            }
        }
        return params.build();
    }

    private static String wrapPort(int port) {
        return (port <= 0 || port == 80) ? "" : String.format(Locale.getDefault(), ":%d", port);
    }

    private static String wrapPath(List<String> pathList, boolean encode) {
        if (pathList.isEmpty()) return "/";
        StringBuilder builder = new StringBuilder();
        for (String path : pathList) {
            builder.append("/").append(encode ? Uri.encode(path) : path);
        }
        return builder.toString();
    }

    private static String wrapQuery(Params params, boolean encode) {
        String query = params.toString(encode);
        return TextUtils.isEmpty(query) ? "" : String.format("?%s", query);
    }

    private static String wrapFragment(String fragment, boolean encode) {
        return TextUtils.isEmpty(fragment) ? "" : String.format("#%s", encode ? Uri.encode(fragment) : fragment);
    }

    public String getScheme() {
        return mScheme;
    }

    public String getHost() {
        return mHost;
    }

    public int getPort() {
        return mPort;
    }

    public String getPath() {
        return mPath;
    }

    public List<String> copyPath() {
        return convertPath(mPath);
    }

    public String getQuery() {
        return mQuery;
    }

    /**
     * @deprecated use {@link #getParams()} instead.
     */
    @Deprecated
    public Params copyQuery() {
        return getParams();
    }

    public Params getParams() {
        return convertQuery(mQuery);
    }

    public String getFragment() {
        return mFragment;
    }

    public Builder builder() {
        return newBuilder(toString());
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean encode) {
        String query = wrapQuery(convertQuery(mQuery), encode);
        String fragment = wrapFragment(mFragment, encode);
        return mScheme + "://" + mHost + wrapPort(mPort) + mPath + query + fragment;
    }

    public Url location(String location) {
        if (TextUtils.isEmpty(location)) return null;

        if (URLUtil.isNetworkUrl(location)) {
            return newBuilder(location).build();
        }

        URI newUri = URI.create(location);
        if (location.startsWith("/")) {
            return builder().setPath(newUri.getPath())
                    .setQuery(newUri.getQuery())
                    .setFragment(newUri.getFragment())
                    .build();
        } else if (location.contains("../")) {
            List<String> oldPathList = convertPath(getPath());
            List<String> newPathList = convertPath(newUri.getPath());

            int start = newPathList.lastIndexOf("..");

            newPathList = newPathList.subList(start + 1, newPathList.size());

            if (!oldPathList.isEmpty()) {
                oldPathList = oldPathList.subList(0, oldPathList.size() - start - 2);
                oldPathList.addAll(newPathList);
                String path = TextUtils.join("/", oldPathList);
                return builder().setPath(path).setQuery(newUri.getQuery()).setFragment(newUri.getFragment()).build();
            }

            String path = TextUtils.join("/", newPathList);

            return builder().setPath(path).setQuery(newUri.getQuery()).setFragment(newUri.getFragment()).build();
        } else {
            String path = (getPath() + newUri.getPath()).replace("//", "/");
            return builder().setPath(path).setQuery(newUri.getQuery()).setFragment(newUri.getFragment()).build();
        }
    }

    public static class Builder {

        private String mScheme;
        private String mHost;
        private int mPort;
        private String mPath;
        private Params.Builder mQuery;
        private String mFragment;

        private Builder(String url) {
            URI uri = URI.create(url);

            this.mScheme = uri.getScheme();
            this.mHost = uri.getHost();
            this.mPort = convertPort(uri.getPort());
            this.mPath = uri.getPath();
            this.mQuery = convertQuery(uri.getQuery()).builder();
            this.mFragment = uri.getFragment();
        }

        public Builder setScheme(String scheme) {
            this.mScheme = scheme;
            return this;
        }

        public Builder setHost(String host) {
            this.mHost = host;
            return this;
        }

        public Builder setPort(int port) {
            this.mPort = port;
            return this;
        }

        public Builder addPath(int value) {
            return addPath(Integer.toString(value));
        }

        public Builder addPath(long value) {
            return addPath(Long.toString(value));
        }

        public Builder addPath(boolean value) {
            return addPath(Boolean.toString(value));
        }

        public Builder addPath(char value) {
            return addPath(String.valueOf(value));
        }

        public Builder addPath(double value) {
            return addPath(Double.toString(value));
        }

        public Builder addPath(float value) {
            return addPath(Float.toString(value));
        }

        public Builder addPath(String path) {
            mPath = mPath + "/" + path;
            return this;
        }

        public Builder setPath(String path) {
            mPath = path;
            return this;
        }

        public Builder clearPath() {
            mPath = "";
            return this;
        }

        public Builder addQuery(String key, int value) {
            return addQuery(key, Integer.toString(value));
        }

        public Builder addQuery(String key, long value) {
            return addQuery(key, Long.toString(value));
        }

        public Builder addQuery(String key, boolean value) {
            return addQuery(key, Boolean.toString(value));
        }

        public Builder addQuery(String key, char value) {
            return addQuery(key, String.valueOf(value));
        }

        public Builder addQuery(String key, double value) {
            return addQuery(key, Double.toString(value));
        }

        public Builder addQuery(String key, float value) {
            return addQuery(key, Float.toString(value));
        }

        public Builder addQuery(String key, short value) {
            return addQuery(key, Integer.toString(value));
        }

        public Builder addQuery(String key, String value) {
            mQuery.add(key, value);
            return this;
        }

        public Builder addQuery(String key, List<String> values) {
            for (String value : values) {
                addQuery(key, value);
            }
            return this;
        }

        public Builder addQuery(Params query) {
            for (Map.Entry<String, List<Object>> entry : query.entrySet()) {
                String key = entry.getKey();
                List<Object> values = entry.getValue();
                for (Object value : values) {
                    if (value instanceof CharSequence) {
                        String textValue = value.toString();
                        addQuery(key, textValue);
                    }
                }
            }
            return this;
        }

        public Builder setQuery(String query) {
            mQuery = convertQuery(query).builder();
            return this;
        }

        public Builder setQuery(Params query) {
            mQuery = query.builder();
            return this;
        }

        public Builder removeQuery(String key) {
            mQuery.remove(key);
            return this;
        }

        public Builder clearQuery() {
            mQuery.clear();
            return this;
        }

        public Builder setFragment(String fragment) {
            this.mFragment = fragment;
            return this;
        }

        public Url build() {
            return new Url(this);
        }
    }
}