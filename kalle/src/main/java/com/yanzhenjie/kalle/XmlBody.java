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

import java.nio.charset.Charset;

import static com.yanzhenjie.kalle.Headers.VALUE_APPLICATION_XML;

/**
 * Created by Zhenjie Yan on 2018/2/11.
 */
public class XmlBody extends StringBody {

    public XmlBody(String body) {
        this(body, Kalle.getConfig().getCharset());
    }

    public XmlBody(String body, Charset charset) {
        super(body, charset, VALUE_APPLICATION_XML);
    }
}