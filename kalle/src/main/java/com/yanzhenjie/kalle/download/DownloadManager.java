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
package com.yanzhenjie.kalle.download;

import com.yanzhenjie.kalle.NetCancel;

/**
 * Created by Zhenjie Yan on 2018/3/18.
 */
public class DownloadManager {

    private static DownloadManager sInstance;

    private DownloadManager() {
    }

    public static DownloadManager getInstance() {
        if (sInstance == null) {
            synchronized (DownloadManager.class) {
                if (sInstance == null) {
                    sInstance = new DownloadManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * Execute a request.
     *
     * @param download download request.
     * @return download the completed file path.
     */
    public String perform(UrlDownload download) throws Exception {
        UrlWorker worker = new UrlWorker(download);
        NetCancel.INSTANCE.add(download.uid(), worker);
        return worker.call();
    }

    /**
     * Execute a request.
     *
     * @param download download request.
     * @return download the completed file path.
     */
    public String perform(BodyDownload download) throws Exception {
        BodyWorker worker = new BodyWorker(download);
        NetCancel.INSTANCE.add(download.uid(), worker);
        return worker.call();
    }
}