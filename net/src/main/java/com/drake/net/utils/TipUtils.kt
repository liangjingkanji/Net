/*
 * MIT License
 *
 * Copyright (c) 2023 劉強東 https://github.com/liangjingkanji
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.drake.net.utils

import android.annotation.SuppressLint
import android.widget.Toast
import com.drake.net.NetConfig

object TipUtils {

    private var toast: Toast? = null

    /**
     * 重复显示不会覆盖, 可以在子线程显示
     * 本方法会导致报内存泄露, 这是因为为了避免吐司反复显示导致重叠会长期持有Toast引用来保持单例导致, 可以无视或者自己实现吐司
     */
    @SuppressLint("ShowToast")
    @JvmStatic
    fun toast(message: String?) {
        message ?: return
        runMain {
            toast?.cancel()
            toast = Toast.makeText(NetConfig.app, message, Toast.LENGTH_SHORT)
            toast?.show()
        }
    }
}