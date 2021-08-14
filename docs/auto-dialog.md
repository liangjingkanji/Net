Net支持发起请求的时候自动弹出和关闭对话框(Loading Dialog)

## 自动显示加载框

只需要使用`scopeDialog`作用域即可.
```kotlin
scopeDialog {
    tvFragment.text = Post<String>("dialog") {
        param("u_name", "drake") // 请求参数
        param("pwd", "123456")
    }.await()
}
```

<br>

加载框默认使用的是Android原生加载框(MaterialDesign Dialog), 当然也提供参数传入指定每个请求的对话框

<img src="https://i.loli.net/2021/08/14/JqIenA56F1cgyHs.png" width="250"/>


## 指定单例加载框

就是仅针对当前网络请求指定加载框

```kotlin
val dialog = BubbleDialog(requireActivity(), "加载中")

scopeDialog(dialog) {
    binding.tvFragment.text = Post<String>("dialog") {
        param("u_name", "drake")
        param("pwd", "123456")
    }.await()
}
```

<img src="https://i.loli.net/2021/08/14/8eDp7Oz2CQT9Jcq.gif" width="250"/>

> 这里使用的iOS风格对话框: [BubbleDialog](https://liangjingkanji.github.io/Tooltip/bubble.html)

## 指定全局加载框

在Application中配置Net时就可以设置默认的Dialog

=== "初始配置全局加载框"
    ```kotlin
    NetConfig.init("http://github.com/") {
            setDialogFactory { // 全局加载对话框
                ProgressDialog(it).apply {
                    setMessage("我是全局自定义的加载对话框...")
                }
            }
    }
    ```
=== "修改全局加载框"
    ```kotlin
    NetConfig.dialogFactory = NetDialogFactory {
        ProgressDialog(it).apply {
            setMessage("我是全局自定义的加载对话框...")
        }
    }
    ```

<br>

如果不想修改全局加载框样式只是修改加载框文本, 可以覆盖文本(国际化同理)

在当前项目下的values目录下的strings.xml添加以下一行可以修改文本

```xml
<!--对话框-->
<string name="net_dialog_msg">加载中</string>
```

## 生命周期

使用`scopeDialog`发起请求后, Dialog分为以下三个生命周期

|生命周期|描述|
|-|-|
|Dialog 显示|执行`scopeDialog`时显示加载框|
|Dialog 自动结束|作用域内任务结束时关闭加载框|
|Dialog 手动结束|加载框被手动取消时取消作用域内网络请求|

## 自定义加载对话框

我想要自定义加载框视图

- Dialog属于布局容器, 你可以继承Dialog然后创建属于自己的显示内容(类似Activity/Fragment), 比如该[iOS风格对话框](https://github.com/liangjingkanji/Tooltip/blob/HEAD/tooltip/src/main/java/com/drake/tooltip/dialog/BubbleDialog.kt)

<br>

我的加载框不是Dialog

- 虽然我们指定`scopeDialog`的加载框或者`setNetDialogFactory`时只允许传入一个Dialog对象, 但即使你使用的不是Dialog你也可以创建一个类继承Dialog, 然后在其生命周期函数中处理`自己特殊对话框`的展示和隐藏

