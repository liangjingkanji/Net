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
package com.yanzhenjie.kalle.cookie.db;

/**
 * Created by Zhenjie Yan on 2018/2/20.
 */
public interface Field {
    String TABLE_NAME = "COOKIES_TABLE";

    String ID = "_ID";
    String URL = "URL";
    String NAME = "NAME";
    String VALUE = "VALUE";
    String COMMENT = "COMMENT";
    String COMMENT_URL = "COMMENT_URL";
    String DISCARD = "DISCARD";
    String DOMAIN = "DOMAIN";
    String EXPIRY = "EXPIRY";
    String PATH = "PATH";
    String PORT_LIST = "PORT_LIST";
    String SECURE = "SECURE";
    String VERSION = "VERSION";
}