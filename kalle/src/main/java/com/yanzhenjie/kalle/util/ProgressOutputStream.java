package com.yanzhenjie.kalle.util;

import com.yanzhenjie.kalle.Content;
import com.yanzhenjie.kalle.ProgressBar;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Zhenjie Yan on 2019-06-24.
 */
public class ProgressOutputStream<T extends Content> extends OutputStream {

    private OutputStream mWriter;
    private T mOrigin;
    private ProgressBar<T> mProgressBar;

    private long mContentLength;
    private long mWriteCount;
    private int mOldProgress;


    public ProgressOutputStream(OutputStream writer, T origin, ProgressBar<T> progressBar) {
        this.mWriter = writer;
        this.mOrigin = origin;
        this.mProgressBar = progressBar;

        this.mContentLength = mOrigin.contentLength();
    }

    @Override
    public void write(int b) throws IOException {
        mWriter.write(b);
        mWriteCount += 1;
        calcProgress();
    }

    @Override
    public void write(byte[] b) throws IOException {
        mWriter.write(b);
        mWriteCount += b.length;
        calcProgress();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        mWriter.write(b, off, len);
        mWriteCount += len;
        calcProgress();
    }

    @Override
    public void flush() throws IOException {
        mWriter.flush();
    }

    @Override
    public void close() throws IOException {
        mWriter.close();
    }

    private void calcProgress() {
        if (mContentLength > 0) {
            int progress = (int) (mWriteCount * 100 / mContentLength);
            if (progress > mOldProgress && progress % 2 == 0) {
                mOldProgress = progress;
                mProgressBar.progress(mOrigin, mOldProgress);
            }
        }
    }
}
