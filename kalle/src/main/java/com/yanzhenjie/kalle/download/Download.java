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
package com.yanzhenjie.kalle.download;

import com.yanzhenjie.kalle.Headers;
import com.yanzhenjie.kalle.Url;

/**
 * Created by Zhenjie Yan on 2018/3/18.
 */
public interface Download {

    /**
     * Get the file download address.
     */
    Url url();

    /**
     * Get headers.
     */
    Headers headers();

    /**
     * Get the directory where the file is to be saved.
     */
    String directory();

    /**
     * Get the file name.
     */
    String fileName();

    /**
     * Get onProgress bar.
     */
    ProgressBar progressBar();

    /**
     * Get download policy.
     */
    Policy policy();

    interface Policy {

        Policy DEFAULT = new Policy() {
            @Override
            public boolean isRange() {
                return true;
            }

            @Override
            public boolean allowDownload(int code, Headers headers) {
                return true;
            }

            @Override
            public boolean oldAvailable(String path, int code, Headers headers) {
                return false;
            }
        };

        /**
         * Does it support breakpoints?
         */
        boolean isRange();

        /**
         * Can I download it?
         *
         * @param code    http response code.
         * @param headers http response headers.
         * @return return true to continue the download, return false will call back the download failed.
         */
        boolean allowDownload(int code, Headers headers);

        /**
         * Discover old files. The file will be returned if it is available,
         * the file will be deleted if it is not available.
         *
         * @param path    old file path.
         * @param code    http response code.
         * @param headers http response headers.
         * @return return true if the old file is available, other wise is false.
         */
        boolean oldAvailable(String path, int code, Headers headers);
    }

    interface ProgressBar {

        ProgressBar DEFAULT = new ProgressBar() {
            @Override
            public void onProgress(int progress, long byteCount, long speed) {
            }
        };

        /**
         * Download onProgress changes.
         */
        void onProgress(int progress, long byteCount, long speed);
    }
}