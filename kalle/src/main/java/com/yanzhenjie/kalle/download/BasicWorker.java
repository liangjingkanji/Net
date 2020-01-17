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

import android.text.TextUtils;

import com.yanzhenjie.kalle.Headers;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.Url;
import com.yanzhenjie.kalle.exception.DownloadError;
import com.yanzhenjie.kalle.util.IOUtils;
import com.yanzhenjie.kalle.util.UrlUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import static com.yanzhenjie.kalle.Headers.KEY_RANGE;

/**
 * Created by Zhenjie Yan on 2018/3/18.
 */
public abstract class BasicWorker<T extends Download> implements Callable<String> {

    private final T mDownload;

    private String mDirectory;
    private String mFileName;
    private Download.ProgressBar mProgressBar;
    private Download.Policy mPolicy;

    BasicWorker(T download) {
        this.mDownload = download;
        this.mDirectory = mDownload.directory();
        this.mFileName = mDownload.fileName();
        this.mProgressBar = new AsyncProgressBar(mDownload.progressBar());
        this.mPolicy = mDownload.policy();
    }

    @Override
    public String call() throws Exception {
        if (TextUtils.isEmpty(mDirectory)) throw new IOException("Please specify the directory.");
        File directory = new File(mDirectory);
        IOUtils.createFolder(directory);

        Response response = null;
        try {
            int code;
            Headers comeHeaders;
            File tempFile;

            if (TextUtils.isEmpty(mFileName)) {
                response = requestNetwork(mDownload);
                code = response.code();
                comeHeaders = response.headers();
                mFileName = getRealFileName(comeHeaders);
                tempFile = new File(mDirectory, mFileName + ".kalle");
            } else {
                tempFile = new File(mDirectory, mFileName + ".kalle");
                if (mPolicy.isRange() && tempFile.exists()) {
                    Headers toHeaders = mDownload.headers();
                    toHeaders.set(KEY_RANGE, "bytes=" + tempFile.length() + "-");
                    response = requestNetwork(mDownload);
                    code = response.code();
                    comeHeaders = response.headers();
                } else {
                    response = requestNetwork(mDownload);
                    code = response.code();
                    comeHeaders = response.headers();

                    IOUtils.delFileOrFolder(tempFile);
                }
            }

            if (!mPolicy.allowDownload(code, comeHeaders)) {
                throw new DownloadError(code, comeHeaders, mDownload.request(), "The download policy prohibits the program from continuing to download");
            }

            File file = new File(mDirectory, mFileName);
            if (file.exists()) {
                String filePath = file.getAbsolutePath();
                if (mPolicy.oldAvailable(filePath, code, comeHeaders)) {
                    mProgressBar.onProgress(100, file.length(), 0);
                    return filePath;
                } else {
                    IOUtils.delFileOrFolder(file);
                }
            }

            long contentLength;

            if (code == 206) {
                String range = comeHeaders.getContentRange();
                contentLength = Long.parseLong(range.substring(range.indexOf('/') + 1));
            } else {
                IOUtils.createNewFile(tempFile);
                contentLength = comeHeaders.getContentLength();
            }

            long oldCount = tempFile.length();
            int oldProgress = 0;
            long oldSpeed = 0;

            RandomAccessFile randomFile = new RandomAccessFile(tempFile, "rws");
            randomFile.seek(oldCount);

            int len;
            byte[] buffer = new byte[8096];

            long speedTime = System.currentTimeMillis();
            long speedCount = 0;

            InputStream stream = response.body().stream();

            while (((len = stream.read(buffer)) != -1)) {
                randomFile.write(buffer, 0, len);

                oldCount += len;
                speedCount += len;

                long totalTime = System.currentTimeMillis() - speedTime;
                if (totalTime < 400) continue;

                long speed = speedCount * 1000 / totalTime;

                if (contentLength != 0) {
                    int progress = (int) (oldCount * 100 / contentLength);
                    if (progress != oldProgress || speed != oldSpeed) {
                        oldProgress = progress;
                        oldSpeed = speed;
                        speedCount = 0;
                        speedTime = System.currentTimeMillis();

                        mProgressBar.onProgress(oldProgress, oldCount, oldSpeed);
                    }
                } else if (oldSpeed != speed) {
                    speedCount = 0;
                    oldSpeed = speed;
                    speedTime = System.currentTimeMillis();

                    mProgressBar.onProgress(0, oldCount, oldSpeed);
                } else {
                    mProgressBar.onProgress(0, oldCount, oldSpeed);
                }
            }
            mProgressBar.onProgress(100, oldCount, oldSpeed);

            //noinspection ResultOfMethodCallIgnored
            tempFile.renameTo(file);
            return file.getAbsolutePath();
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    /**
     * Perform a network request.
     *
     * @param download target request.
     * @return {@link Response}.
     * @throws IOException when connecting to the network, write data, read the data {@link IOException} occurred.
     */
    protected abstract Response requestNetwork(T download) throws IOException;

    /**
     * Cancel request.
     */
    public abstract void cancel();

    private String getRealFileName(Headers headers) throws IOException {
        String fileName = null;
        String contentDisposition = headers.getContentDisposition();
        if (!TextUtils.isEmpty(contentDisposition)) {
            fileName = Headers.parseSubValue(contentDisposition, "filename", null);
            if (!TextUtils.isEmpty(fileName)) {
                fileName = UrlUtils.urlDecode(fileName, "utf-8");
                if (fileName.startsWith("\"") && fileName.endsWith("\"")) {
                    fileName = fileName.substring(1, fileName.length() - 1);
                }
            }
        }

        // From url.
        if (TextUtils.isEmpty(fileName)) {
            Url url = mDownload.url();
            String path = url.getPath();
            if (TextUtils.isEmpty(path)) {
                fileName = Integer.toString(url.toString().hashCode());
            } else {
                String[] slash = path.split("/");
                fileName = slash[slash.length - 1];
            }
        }
        return fileName;
    }

    private static class AsyncProgressBar implements Download.ProgressBar {

        private final Download.ProgressBar mProgressBar;
        private final Executor mExecutor;

        AsyncProgressBar(Download.ProgressBar bar) {
            this.mProgressBar = bar;
            this.mExecutor = Kalle.getConfig().getMainExecutor();
        }

        @Override
        public void onProgress(final int progress, final long byteCount, final long speed) {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.onProgress(progress, byteCount, speed);
                }
            });
        }
    }
}