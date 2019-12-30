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
package com.yanzhenjie.kalle.cookie;

import android.text.TextUtils;

import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Zhenjie Yan on 2018/2/22.
 */
public class CookieManager {

    private CookieStore cookieJar;

    public CookieManager(CookieStore store) {
        this.cookieJar = store;
    }

    private static int getPort(URI uri) {
        int port = uri.getPort();
        return (port == -1) ? ("https".equals(uri.getScheme()) ? 443 : 80) : port;
    }

    private static boolean containsPort(String portList, int port) {
        if (portList.contains(",")) {
            String[] portArray = portList.split(",");
            String inPort = Integer.toString(port);
            for (String outPort : portArray) {
                if (outPort.equals(inPort)) {
                    return true;
                }
            }
            return false;
        }
        return portList.equalsIgnoreCase(Integer.toString(port));
    }

    private static boolean pathMatches(URI uri, HttpCookie cookie) {
        return normalizePath(uri.getPath()).startsWith(normalizePath(cookie.getPath()));
    }

    private static String normalizePath(String path) {
        if (path == null) {
            path = "";
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * Get the cookie under the specified URI, where https and secure will be verified.
     *
     * @param uri uri.
     * @return all cookies that match the rules.
     */
    public List<String> get(URI uri) {
        boolean secureLink = "https".equalsIgnoreCase(uri.getScheme());
        List<HttpCookie> outCookieList = new ArrayList<>();
        List<HttpCookie> inCookieList = cookieJar.get(uri);
        for (HttpCookie cookie : inCookieList) {
            if (pathMatches(uri, cookie) && (secureLink || !cookie.getSecure())) {
                String portList = cookie.getPortlist();
                int port = getPort(uri);
                if (TextUtils.isEmpty(portList) || containsPort(portList, port)) {
                    outCookieList.add(cookie);
                }
            }
        }
        if (outCookieList.isEmpty()) return Collections.emptyList();

        List<String> cookieList = new ArrayList<>();
        cookieList.add(sortByPath(outCookieList));
        return cookieList;
    }

    /**
     * Cookie for the specified URI to save, where path and port will be verified.
     *
     * @param uri        uri.
     * @param cookieList all you want to save the Cookie, does not meet the rules will not be saved.
     */
    public void add(URI uri, List<String> cookieList) {
        for (String cookieValue : cookieList) {
            List<HttpCookie> cookies = HttpCookie.parse(cookieValue);
            for (HttpCookie cookie : cookies) {
                if (cookie.getPath() == null) {
                    String path = normalizePath(uri.getPath());
                    cookie.setPath(path);
                } else if (!pathMatches(uri, cookie)) {
                    continue;
                }

                if (cookie.getDomain() == null) cookie.setDomain(uri.getHost());

                String portList = cookie.getPortlist();
                int port = getPort(uri);
                if (TextUtils.isEmpty(portList) || containsPort(portList, port)) {
                    cookieJar.add(uri, cookie);
                }
            }
        }
    }

    private String sortByPath(List<HttpCookie> cookies) {
        Collections.sort(cookies, new CookiePathComparator());
        final StringBuilder result = new StringBuilder();
        int minVersion = 1;
        for (HttpCookie cookie : cookies) {
            if (cookie.getVersion() < minVersion) {
                minVersion = cookie.getVersion();
            }
        }

        if (minVersion == 1) {
            result.append("$Version=\"1\"; ");
        }

        for (int i = 0; i < cookies.size(); ++i) {
            if (i != 0) {
                result.append("; ");
            }

            result.append(cookies.get(i).toString());
        }

        return result.toString();
    }


    private static class CookiePathComparator implements Comparator<HttpCookie> {
        @Override
        public int compare(HttpCookie c1, HttpCookie c2) {
            if (c1 == c2) return 0;
            if (c1 == null) return -1;
            if (c2 == null) return 1;

            if (!c1.getName().equals(c2.getName())) return 0;

            final String c1Path = normalizePath(c1.getPath());
            final String c2Path = normalizePath(c2.getPath());

            if (c1Path.startsWith(c2Path)) return -1;
            else if (c2Path.startsWith(c1Path)) return 1;
            else return 0;
        }
    }
}