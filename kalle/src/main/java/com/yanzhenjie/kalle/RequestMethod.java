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
package com.yanzhenjie.kalle;

import java.util.Locale;

/**
 * <p>
 * HTTP handle method.
 * </p>
 * Created in Oct 10, 2015 8:00:48 PM.
 */
public enum RequestMethod {

    GET("GET"),

    POST("POST"),

    PUT("PUT"),

    DELETE("DELETE"),

    HEAD("HEAD"),

    PATCH("PATCH"),

    OPTIONS("OPTIONS"),

    TRACE("TRACE");

    private final String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public static RequestMethod reverse(String method) {
        method = method.toUpperCase(Locale.ENGLISH);
        switch (method) {
            case "GET": {
                return GET;
            }
            case "POST": {
                return POST;
            }
            case "PUT": {
                return PUT;
            }
            case "DELETE": {
                return DELETE;
            }
            case "HEAD": {
                return HEAD;
            }
            case "PATCH": {
                return PATCH;
            }
            case "OPTIONS": {
                return OPTIONS;
            }
            case "TRACE": {
                return TRACE;
            }
            default: {
                return GET;
            }
        }
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean allowBody() {
        switch (this) {
            case POST:
            case PUT:
            case PATCH:
            case DELETE: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

}
