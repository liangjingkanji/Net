Net具备完善的全局错误处理机制

|场景|处理函数|处理方式|
|-|-|-|
|普通网络请求/自动加载框|`onError`|`toast`提示后端定义的错误消息|
|自动缺省页|`onStateError`|部分错误信息`toast`|

缺省页不需要所有的错误信息都吐司(toast)提示, 因为错误页可能已经展示错误信息, 所以这里两者处理的函数区分.

但是所有的错误信息和错误码都会在LogCat控制台看到, 具体查看[异常追踪](exception-track.md)

## 默认错误处理

Net默认的错误处理方式

<img src="https://i.imgur.com/t1Ep8tj.png" width="70%"/>


## 错误信息文本

默认错误处理的文本被定义在`strings.xml`中, 我们可以在项目中使用同名覆盖或者多语言
```xml
<!--网络请求异常-->
<string name="net_network_error">当前网络不可用</string>
<string name="net_url_error">请求资源地址错误</string>
<string name="net_host_error">无法找到指定服务器主机</string>
<string name="net_connect_timeout_error">连接服务器超时，请重试</string>
<string name="net_connect_exception">请检查网络连接</string>
<string name="net_read_exception">读取数据错误</string>
<string name="net_write_exception">发送数据错误</string>
<string name="net_read_timeout_error">读取服务器数据超时，请检查网络</string>
<string name="net_download_error">下载过程发生错误</string>
<string name="net_no_cache_error">读取缓存错误</string>
<string name="net_parse_error">解析数据时发生异常</string>
<string name="net_request_error">请求参数错误</string>
<string name="net_server_error">服务响应错误</string>
<string name="net_image_error">图片下载错误</string>
<string name="net_null_error">数据为空</string>
<string name="net_other_error">未知错误</string>
```