package com.drake.net.sample.utils

import okhttp3.internal.closeQuietly
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

object RandomFileUtils {

    /**
     * 创建指定大小随机内容的文件, 创建失败返回false
     * 如果已经存在指定大小且同名的文件将不再创建
     */
    fun createRandomFile(file: File, size: Long, unit: FileSizeUnit): Boolean {
        val fileLength = unit.getBytes(size)
        var fos: FileOutputStream? = null
        try {
            // 已经存在指定大小且同名的文件结束函数
            if (!file.createNewFile() && file.length() == fileLength) {
                return false
            }
            var batchSize = fileLength.coerceAtMost(FileSizeUnit.KB.length())
            batchSize = batchSize.coerceAtMost(FileSizeUnit.MB.length())
            val count = fileLength / batchSize
            val last = fileLength % batchSize
            fos = FileOutputStream(file)
            val fileChannel = fos.channel
            for (i in 0 until count) {
                val buffer = ByteBuffer.allocate(batchSize.toInt())
                fileChannel.write(buffer)
            }
            if (last != 0L) {
                val buffer = ByteBuffer.allocate(last.toInt())
                fileChannel.write(buffer)
            }
            fos.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fos?.closeQuietly()
        }
        return false
    }

    enum class FileSizeUnit {
        KB, MB, GB;

        fun getBytes(size: Long): Long {
            return size * length()
        }

        fun length(): Long {
            return when (this) {
                KB -> 1024
                MB -> 1024 * 1024
                GB -> 1024 * 1024 * 1024
            }
        }
    }
}