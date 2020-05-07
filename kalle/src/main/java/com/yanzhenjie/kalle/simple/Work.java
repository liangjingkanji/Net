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
package com.yanzhenjie.kalle.simple;

import com.yanzhenjie.kalle.Canceller;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static android.util.Log.d;

/**
 * Created by Zhenjie Yan on 2018/2/13.
 */
final class Work<T extends SimpleRequest, S, F> extends FutureTask<Result<S, F>> implements Canceller {

    private final Callback<S, F> mCallback;
    private BasicWorker<T, S, F> mWorker;

    Work(BasicWorker<T, S, F> work, Callback<S, F> callback) {
        super(work);
        this.mWorker = work;
        this.mCallback = callback;
    }

    @Override
    public void run() {
        mCallback.onStart();
        super.run();
    }

    @Override
    protected void done() {
        try {
            mCallback.onResponse(get());
        } catch (CancellationException e) {
            mCallback.onCancel();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (isCancelled()) {
                mCallback.onCancel();
            } else if (cause != null && cause instanceof Exception) {
                mCallback.onException((Exception) cause);
            } else {
                mCallback.onException(new Exception(cause));
            }
        } catch (Exception e) {
            if (isCancelled()) {
                mCallback.onCancel();
            } else {
                mCallback.onException(e);
            }
        }
        mCallback.onEnd();
    }

    @Override
    public void cancel() {
        cancel(true);
        d("日志", "(Work.java:72)    取消");
        mWorker.cancel();
    }
}