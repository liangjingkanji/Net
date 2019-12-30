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

import com.yanzhenjie.kalle.util.ProgressOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executor;

/**
 * Created by Zhenjie Yan on 2018/2/11.
 */
public abstract class BaseContent<T extends Content> implements Content {

    private ProgressBar<T> mProgressBar;

    public void onProgress(ProgressBar<T> progressBar) {
        this.mProgressBar = new AsyncProgressBar<>(progressBar);
    }

    @Override
    public final void writeTo(OutputStream writer) throws IOException {
        if (mProgressBar != null) {
            onWrite(new ProgressOutputStream<>(writer, (T) this, mProgressBar));
        } else {
            onWrite(writer);
        }
    }

    /**
     * Content body data.
     */
    protected abstract void onWrite(OutputStream writer) throws IOException;

    private static class AsyncProgressBar<T extends Content> implements ProgressBar<T> {

        private final ProgressBar<T> mProgressBar;
        private final Executor mExecutor;

        public AsyncProgressBar(ProgressBar<T> bar) {
            this.mProgressBar = bar;
            this.mExecutor = Kalle.getConfig().getMainExecutor();
        }

        @Override
        public void progress(final T origin, final int progress) {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.progress(origin, progress);
                }
            });
        }
    }

}