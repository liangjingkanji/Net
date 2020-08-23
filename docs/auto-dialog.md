Net支持发起请求的时候自动弹出和关闭对话框(Loading Dialog)


<br>
只需要使用`scopeDialog`作用域即可.
```kotlin
scopeDialog {
    tv_fragment.text = Post<String>("dialog") {
        param("u_name", "drake") // 请求参数
        param("pwd", "123456")
    }.await()
}
```

<br>

加载框默认使用的是Android原生加载框(MaterialDesign Dialog), 当然也提供配置函数
<img src="https://i.imgur.com/egUM3V1.png" width="50%"/>

## 自定义加载框

在Application中配置Net时就可以设置默认的Dialog

=== "初始化"
    ```kotlin
    initNet("http://182.92.97.186/") {
        onDialog { // lambda返回一个Dialog对象
            ProgressDialog(it).apply { // it 为 FragmentActivity
                setMessage("正在努力请求中")
            }
        }
    }
    ```
=== "设置全局"
    ```kotlin
    NetConfig.onDialog = { //
        ProgressDialog(it).apply {
            setMessage("请稍等")
        }
    }
    ```

<br>

如果仅修改加载框文本, 可以覆盖`strings.xml`中定义的文本
```xml
<!--对话框-->
<string name="net_dialog_msg">加载中</string>
```

## 生命周期

|生命周期|描述|
|-|-|
|开始|执行`scopeDialog`时显示加载框|
|自动结束|作用域内任务结束时关闭加载框|
|手动结束|加载框被手动取消时取消作用域内网络请求|
