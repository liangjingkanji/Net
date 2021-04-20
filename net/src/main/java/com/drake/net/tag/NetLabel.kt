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

sealed class NetLabel {
    inline class RequestId(val value: Any?)
    inline class RequestGroup(val value: Any?)
    inline class RecordLog(val enabled: Boolean)

    inline class DownloadFileDir(val dir: String?) {
        constructor(fileDir: File?) : this(fileDir?.absolutePath)
    }

    inline class DownloadFileMD5Verify(val enabled: Boolean = true)
    inline class DownloadFileNameDecode(val enabled: Boolean = true)
    inline class DownloadTempFile(val enabled: Boolean = true)
    inline class DownloadFileConflictRename(val enabled: Boolean = true)
    inline class DownloadFileName(val name: String?)
    class DownloadListeners : ConcurrentLinkedQueue<ProgressListener>()
    class UploadListeners : ConcurrentLinkedQueue<ProgressListener>()
    class TagHashMap : HashMap<String, Any?>()
}
