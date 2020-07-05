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
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.NetCancel;

import java.lang.reflect.Type;
import java.util.concurrent.Executor;

/**
 * Created by Zhenjie Yan on 2018/2/13.
 */
public class RequestManager {

    private static RequestManager sInstance;
    private final Executor mExecutor;

    private RequestManager() {
        this.mExecutor = Kalle.getConfig().getWorkExecutor();
    }

    public static RequestManager getInstance() {
        if (sInstance == null) {
            synchronized (RequestManager.class) {
                if (sInstance == null) {
                    sInstance = new RequestManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * Submit a request to the queue.
     *
     * @param request  request.
     * @param callback accept the result callback.
     * @param <S>      target object parameter.
     * @param <F>      target object parameter.
     * @return this request corresponds to the task cancel handle.
     */
    public <S, F> Canceller perform(final SimpleUrlRequest request, Callback<S, F> callback) {
        final Work<SimpleUrlRequest, S, F> work = new Work<>(new UrlWorker<S, F>(request, callback.getSucceed(), callback.getFailed()), new AsyncCallback<S, F>(callback) {
            @Override
            public void onEnd() {
                super.onEnd();
                NetCancel.INSTANCE.remove(request.uid());
            }
        });
        NetCancel.INSTANCE.add(request.uid(), work);
        mExecutor.execute(work);
        return work;
    }

    /**
     * Execute a request.
     *
     * @param request request.
     * @param succeed the data type when the business succeed.
     * @param failed  the data type when the business failed.
     * @param <S>     target object parameter.
     * @param <F>     target object parameter.
     * @return the response to this request.
     */
    public <S, F> Result<S, F> perform(SimpleUrlRequest request, Type succeed, Type failed) throws Exception {
        UrlWorker<S, F> worker = new UrlWorker<>(request, succeed, failed);
        NetCancel.INSTANCE.add(request.uid(), worker);
        return worker.call();
    }

    /**
     * Submit a request to the queue.
     *
     * @param request  request.
     * @param callback accept the result callback.
     * @param <S>      target object parameter.
     * @param <F>      target object parameter.
     * @return this request corresponds to the task cancel handle.
     */
    public <S, F> Canceller perform(final SimpleBodyRequest request, Callback<S, F> callback) {
        Work<SimpleBodyRequest, S, F> work = new Work<>(new BodyWorker<S, F>(request, callback.getSucceed(), callback.getFailed()), new AsyncCallback<S, F>(callback) {
            @Override
            public void onEnd() {
                super.onEnd();
                NetCancel.INSTANCE.remove(request.uid());
            }
        });
        NetCancel.INSTANCE.add(request.uid(), work);
        mExecutor.execute(work);
        return work;
    }

    /**
     * Execute a request.
     *
     * @param request request.
     * @param succeed the data type when the business succeed.
     * @param failed  the data type when the business failed.
     * @param <S>     target object parameter.
     * @param <F>     target object parameter.
     * @return the response to this request.
     */
    public <S, F> Result<S, F> perform(SimpleBodyRequest request, Type succeed, Type failed) throws Exception {
        BodyWorker<S, F> worker = new BodyWorker<>(request, succeed, failed);
        NetCancel.INSTANCE.add(request.uid(), worker);
        return worker.call();
    }

    private static class AsyncCallback<S, F> extends Callback<S, F> {

        private final Callback<S, F> mCallback;
        private final Executor mExecutor;

        AsyncCallback(Callback<S, F> callback) {
            this.mCallback = callback;
            this.mExecutor = Kalle.getConfig().getMainExecutor();
        }

        @Override
        public Type getSucceed() {
            return mCallback.getSucceed();
        }

        @Override
        public Type getFailed() {
            return mCallback.getFailed();
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
        public void onResponse(final Result<S, F> response) {
            if (mCallback == null) return;
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mCallback.onResponse(response);
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