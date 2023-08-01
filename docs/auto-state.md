!!! success "模块化依赖"
    如果自己处理缺省页可跳过本章, Net可以仅仅作为简单的网络框架存在

<br>
Net可依赖三方库实现自动缺省页, 以下二选一依赖

1. 依赖 [StateLayout](https://github.com/liangjingkanji/StateLayout) <a href="https://jitpack.io/#liangjingkanji/StateLayout"><img src="https://jitpack.io/v/liangjingkanji/StateLayout.svg"/></a><br>
    ```groovy
    implementation 'com.github.liangjingkanji:StateLayout:+' // 使用固定版本号替换+符号
    ```
1. 依赖 [BRV](https://github.com/liangjingkanji/BRV) (因为BRV包含StateLayout) <a href="https://jitpack.io/#liangjingkanji/BRV"><img src="https://jitpack.io/v/liangjingkanji/BRV.svg"/></a><br>
    ```groovy
    implementation 'com.github.liangjingkanji:BRV:+' // 使用固定版本号替换+符号
    ```

## 初始化
在Application中初始化缺省页

````kotlin
StateConfig.apply {
    emptyLayout = R.layout.layout_empty
    loadingLayout = R.layout.layout_loading
    errorLayout = R.layout.layout_error
}
````

## 创建

使用`StateLayout`包裹的内容即`内容`(content)

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

## 网络请求

1. 请求开始, 显示`加载中`缺省页
2. 请求成功, 显示`内容`缺省页
3. 请求失败,  显示`错误`缺省页

```kotlin
state.onRefresh {
    scope {
        tvFragment.text = Get<String>("api").await()
    }
}.showLoading()
```
<br>


## 生命周期

| 生命周期 | 描述                                           |
| -------- | ---------------------------------------------- |
| 开始     | `showLoading`触发`onRefresh`, 开始请求 |
| 结束     | 缺省页被销毁, 请求自动取消                 |