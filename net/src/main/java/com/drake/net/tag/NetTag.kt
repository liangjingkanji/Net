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

package com.drake.net.tag

import com.drake.net.interfaces.ProgressListener
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KType

sealed class NetTag {
    class Extras : HashMap<String, Any?>()

    inline class RequestId(val value: Any?)
    inline class RequestGroup(val value: Any?)
    inline class RequestKType(val value: KType?)
    inline class LogRecord(val enabled: Boolean)

    inline class DownloadFileMD5Verify(val enabled: Boolean = true)
    inline class DownloadFileNameDecode(val enabled: Boolean = true)
    inline class DownloadTempFile(val enabled: Boolean = true)
    inline class DownloadFileConflictRename(val enabled: Boolean = true)
    inline class DownloadFileName(val name: String?)
    inline class DownloadFileDir(val dir: String?) {
        constructor(fileDir: File?) : this(fileDir?.absolutePath)
    }

    class UploadListeners : ConcurrentLinkedQueue<ProgressListener>()
    class DownloadListeners : ConcurrentLinkedQueue<ProgressListener>()
}
