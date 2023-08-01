Net支持发起请求开始时显示加载框, 请求结束时隐藏加载框(无论成败)

## 自动显示


```kotlin hl_lines="1"
scopeDialog {
    tvFragment.text = Post<String>("dialog") {
        param("u_name", "drake") // 请求参数
        param("pwd", "123456")
    }.await()
}
```

<br>

默认是原生加载框(MaterialDesign Dialog), 可自定义

<img src="https://i.loli.net/2021/08/14/JqIenA56F1cgyHs.png" width="250"/>


## 单例自定义

指定当前请求加载框

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

!!! quote "菊花加载对话框"
    示例使用的iOS风格对话框: [BubbleDialog](https://liangjingkanji.github.io/Tooltip/bubble.html)

## 全局自定义

初始化时指定加载对话框构造器`NetDialogFactory`

```kotlin
NetConfig.initialize(Api.HOST, this) {
        setDialogFactory {
            ProgressDialog(it).apply {
                setMessage("我是全局自定义的加载对话框...")
            }
        }
}
```

如仅修改加载对话框文本, 在项目`values`目录的strings.xml添加以下

```xml
<!--对话框-->
<string name="net_dialog_msg">加载中</string>
```

!!! question "自定义的加载框不是Dialog"
    由于`scopeDialog`只能指定Dialog类型, 因此只能手动实现`Dialog`接口

    仅要求复写 [DialogCoroutineScope](https://github.com/liangjingkanji/Net/blob/2abf07e1d003ef44574278fd2010f3375225d964/net/src/main/java/com/drake/net/scope/DialogCoroutineScope.kt#L47) 内调用的`Dialog`方法

## 生命周期

使用`scopeDialog`发起请求后, 分为三个生命周期

| 加载框状态 | 作用域                        |
| ---------- | ----------------------------- |
| 显示       | 执行`scopeDialog`时显示加载框 |
| 隐藏       | 作用域内任务结束时隐藏加载框  |
| 手动取消   | 取消作用域内所有网络请求      |





