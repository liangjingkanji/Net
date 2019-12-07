/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/7/19 1:08 PM
 */

package com.drake.net.observable;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.DeferredScalarDisposable;


public final class FromObservable<T> extends Observable<T> implements Callable<T> {
    final Callable<? extends T> callable;

    public FromObservable(Callable<? extends T> callable) {
        this.callable = callable;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {

        DeferredScalarDisposable<T> d = new DeferredScalarDisposable<T>(observer);
        observer.onSubscribe(d);
        if (d.isDisposed()) {
            return;
        }
        T value;
        try {
            value = ObjectHelper.requireNonNull(callable.call(), "Callable returned null");
        } catch (Throwable e) {
            e.printStackTrace();
            Exceptions.throwIfFatal(e);
            return;
        }

        d.complete(value);
    }

    @Override
    public T call() throws Exception {
        return ObjectHelper.requireNonNull(callable.call(), "The callable returned a null value");
    }
}
