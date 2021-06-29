考虑到低耦合, 所以自定义缺省页需要导入第三方组件依赖(点击链接按照文档依赖), 当然如果你使用其他方式处理缺省页可以跳过本章.

依赖以下两种库其中之一即可支持自动显示缺省页

1. 依赖 [StateLayout](https://github.com/liangjingkanji/StateLayout) <br>
    <a href="https://jitpack.io/#liangjingkanji/StateLayout"><img src="https://jitpack.io/v/liangjingkanji/StateLayout.svg"/></a><br>
    使用固定版本号替换+符号
    ```groovy
    implementation 'com.github.liangjingkanji:StateLayout:+'
    ```
1. 依赖 [BRV](https://github.com/liangjingkanji/BRV) (因为BRV包含StateLayout) <br>
    <a href="https://jitpack.io/#liangjingkanji/BRV"><img src="https://jitpack.io/v/liangjingkanji/BRV.svg"/></a><br>
    使用固定版本号替换+符号
    ```groovy
    implementation 'com.github.liangjingkanji:BRV:+'
    ```


<br>
创建缺省页

```xml
<com.drake.statelayout.StateLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:id="@+id/state"
    android:layout_height="match_parent"
    tools:context=".ui.fragment.StateLayoutFragment">

    <TextView
        android:id="@+id/tvFragment"
        android:padding="32dp"
        android:textStyle="bold"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="内容" />

</com.drake.statelayout.StateLayout>
```

自动显示缺省页

```kotlin
state.onRefresh {
    scope {
        tvFragment.text = Get<String>("api").await()
    }
}.showLoading()
```
<br>

> 注意高亮处使用的是`scope`而不是其他作用域, 只能使用scope, 否则无法跟随StateLayout生命周期(自动显示对应缺省页)等功能

## 生命周期

|生命周期|描述|
|-|-|
|开始|StateLayout执行`showLoading`后触发`onRefresh`, 然后开始网络请求|
|结束|缺省页被销毁(例如其所在的Activity或Fragment被销毁), 网络请求自动取消|



