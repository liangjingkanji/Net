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

import com.yanzhenjie.kalle.Canceller;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.NetCancel;

import java.util.concurrent.Executor;

/**
 * Created by Zhenjie Yan on 2018/3/18.
 */
public class DownloadManager {

    private static DownloadManager sInstance;
    private final Executor mExecutor;

    private DownloadManager() {
        this.mExecutor = Kalle.getConfig().getWorkExecutor();
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
     * Submit a request to the queue.
     *
     * @param download download request.
     * @param callback accept the result callback.
     * @return this request corresponds to the task cancel handle.
     */
    public Canceller perform(final UrlDownload download, Callback callback) {
        final Work<UrlDownload> work = new Work<>(new UrlWorker(download), new AsyncCallback(callback) {
            @Override
            public void onEnd() {
                super.onEnd();
                NetCancel.INSTANCE.remove(download.uid());
            }
        });
        NetCancel.INSTANCE.add(download.uid(), work);
        mExecutor.execute(work);
        return work;
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
     * Submit a request to the queue.
     *
     * @param download download request.
     * @param callback accept the result callback.
     * @return this request corresponds to the task cancel handle.
     */
    public Canceller perform(final BodyDownload download, Callback callback) {
        final Work<BodyDownload> work = new Work<>(new BodyWorker(download), new AsyncCallback(callback) {
            @Override
            public void onEnd() {
                super.onEnd();
                NetCancel.INSTANCE.remove(download.uid());
            }
        });
        NetCancel.INSTANCE.add(download.uid(), work);
        mExecutor.execute(work);
        return work;
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

    private static class AsyncCallback implements Callback {

        private final Callback mCallback;
        private final Executor mExecutor;

        AsyncCallback(Callback callback) {
            this.mCallback = callback;
            this.mExecutor = Kalle.getConfig().getMainExecutor();
        }

        @Override
        public void onStart() {
            if (mCallback == null) return;
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mCallback.onStart();
                }
            });
        }

        @Override
        public void onFinish(final String path) {
            if (mCallback == null) return;
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mCallback.onFinish(path);
                }
            });
        }

        @Override
        public void onException(final Exception e) {
            if (mCallback == null) return;
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mCallback.onException(e);
                }
            });
        }

        @Override
        public void onCancel() {
            if (mCallback == null) return;
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mCallback.onCancel();
                }
            });
        }

        @Override
        public void onEnd() {
            if (mCallback == null) return;
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mCallback.onEnd();
                }
            });
        }
    }
}