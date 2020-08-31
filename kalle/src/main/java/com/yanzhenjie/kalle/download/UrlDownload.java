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

import com.yanzhenjie.kalle.Canceller;
import com.yanzhenjie.kalle.Request;
import com.yanzhenjie.kalle.RequestMethod;
import com.yanzhenjie.kalle.Url;
import com.yanzhenjie.kalle.UrlRequest;

/**
 * Created by Zhenjie Yan on 2018/3/18.
 */
public class UrlDownload extends UrlRequest implements Download {

    private final String mDirectory;
    private final String mFileName;
    private final ProgressBar mProgressBar;
    private final Policy mPolicy;

    private UrlDownload(Api api) {
        super(api);
        this.mDirectory = api.mDirectory;
        this.mFileName = api.mFileName;
        this.mProgressBar = api.mProgressBar == null ? ProgressBar.DEFAULT : api.mProgressBar;
        this.mPolicy = api.mPolicy == null ? Policy.DEFAULT : api.mPolicy;
    }

    public static UrlDownload.Api newApi(Url url, RequestMethod method) {
        return new UrlDownload.Api(url, method);
    }

    @Override
    public Request request() {
        return this;
    }

    @Override
    public String directory() {
        return mDirectory;
    }

    @Override
    public String fileName() {
        return mFileName;
    }

    @Override
    public ProgressBar progressBar() {
        return mProgressBar;
    }

    @Override
    public Policy policy() {
        return mPolicy;
    }

    public static class Api extends UrlRequest.Api<UrlDownload.Api> {

        private String mDirectory;
        private String mFileName;

        private ProgressBar mProgressBar;
        private Policy mPolicy;

        private Api(Url url, RequestMethod method) {
            super(url, method);
        }

        public Api directory(String directory) {
            this.mDirectory = directory;
            return this;
        }

        public Api fileName(String fileName) {
            this.mFileName = fileName;
            return this;
        }

        public Api onProgress(ProgressBar bar) {
            this.mProgressBar = bar;
            return this;
        }

        public Api policy(Policy policy) {
            this.mPolicy = policy;
            return this;
        }

        public String perform() throws Exception {
            return DownloadManager.getInstance().perform(new UrlDownload(this));
        }

        public Canceller perform(Callback callback) {
            return DownloadManager.getInstance().perform(new UrlDownload(this), callback);
        }
    }
}