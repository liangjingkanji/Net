!!! question "网络错误提示"
    网络请求发生错误一定要提示给用户, 并且是可读语义句

修改默认吐司错误文本或国际化参考以下方式

## 创建多语言

错误提示文本被定义在`strings.xml`, 在项目中创建同名`name`可复写Net定义的文本

国际化语言则需创建多语言`values`, 例如英语是`values-en`下创建`strings.xml`

??? example "错误文本"
    ```xml
        <!--网络请求异常-->
        <string name="net_connect_error">连接网络失败</string>
        <string name="net_url_error">请求资源地址错误</string>
        <string name="net_host_error">无法找到指定服务器主机</string>
        <string name="net_connect_timeout_error">连接服务器超时，%s</string>
        <string name="net_download_error">下载过程发生错误</string>
        <string name="net_no_cache_error">读取缓存失败</string>
        <string name="net_parse_error">解析数据时发生异常</string>
        <string name="request_failure">请求失败</string>
        <string name="net_request_error">请求参数错误</string>
        <string name="net_server_error">服务响应错误</string>
        <string name="net_null_error">发生空异常</string>
        <string name="net_error">未知网络错误</string>
        <string name="net_other_error">未知错误</string>
        <string name="no_error_message">无错误信息</string>

        <!--对话框-->
        <string name="net_dialog_msg">加载中</string>
    ```

## 创建NetErrorHandler
使用[自定义全局错误处理](error-global.md)可完全修改, 可不提示错误或附上错误信息

??? example "全局错误处理"
    ```kotlin
    fun onError(e: Throwable) {
        val message = when (e) {
            is UnknownHostException -> NetConfig.app.getString(R.string.net_host_error)
            is URLParseException -> NetConfig.app.getString(R.string.net_url_error)
            is NetConnectException -> NetConfig.app.getString(R.string.net_connect_error)
            is NetSocketTimeoutException -> NetConfig.app.getString(
                R.string.net_connect_timeout_error,
                e.message
            )
            is DownloadFileException -> NetConfig.app.getString(R.string.net_download_error)
            is ConvertException -> NetConfig.app.getString(R.string.net_parse_error)
            is RequestParamsException -> NetConfig.app.getString(R.string.net_request_error)
            is ServerResponseException -> NetConfig.app.getString(R.string.net_server_error)
            is NullPointerException -> NetConfig.app.getString(R.string.net_null_error)
            is NoCacheException -> NetConfig.app.getString(R.string.net_no_cache_error)
            is ResponseException -> e.message
            is HttpFailureException -> NetConfig.app.getString(R.string.request_failure)
            is NetException -> NetConfig.app.getString(R.string.net_error)
            else -> NetConfig.app.getString(R.string.net_other_error)
        }

        Net.debug(e)
        TipUtils.toast(message)
    }
    ```
