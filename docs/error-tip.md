如果你仅仅是想修改默认吐司的错误文本信息或者做国际化语言

## 复写NetErrorHandler
这实际上就是自定义全局错误处理. 不过你可以复制默认的实现仅修改下文本信息即可

<img src="https://s2.loli.net/2022/03/01/mQNuoUwtxfK8P6E.png" width="500"/>


## 复写XML

默认错误处理的文本被定义在`strings.xml`中, 我们可以在项目中使用同名覆盖或者多语言

```xml
<!--网络请求异常-->
<string name="net_network_error">当前网络不可用</string>
<string name="net_url_error">请求资源地址错误</string>
<string name="net_host_error">无法找到指定服务器主机</string>
<string name="net_connect_timeout_error">连接服务器超时，%s</string>
<string name="net_download_error">下载过程发生错误</string>
<string name="net_no_cache_error">读取缓存错误</string>
<string name="net_parse_error">解析数据时发生异常</string>
<string name="net_request_error">请求参数错误</string>
<string name="net_server_error">服务响应错误</string>
<string name="net_null_error">数据为空</string>
<string name="net_error">未知网络错误</string>
<string name="net_other_error">未知错误</string>
<string name="no_error_message">无错误信息</string>
```