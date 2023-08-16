!!! success "模块化依赖"
    如果自己处理下拉刷新可跳过本章, Net可以仅仅作为简单的网络框架存在

<br>
Net可依赖三方库 [BRV](https://github.com/liangjingkanji/BRV) 实现自动处理下拉刷新

<a href="https://jitpack.io/#liangjingkanji/BRV"><img src="https://jitpack.io/v/liangjingkanji/BRV.svg"/></a>

```groovy
implementation 'com.github.liangjingkanji:BRV:+' // 使用固定版本号替换+符号
```

## PageRefreshLayout

```xml
<com.drake.brv.PageRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/page"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.fragment.PushRefreshFragment">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rv_push"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</com.drake.brv.PageRefreshLayout>
```

## 创建列表

```kotlin
rv_push.linear().setup {
    addType<String>(R.layout.item_list)
}
```

## 网络请求

1. 请求开始, 显示下拉刷新动画
2. 请求成功, 显示`内容`缺省页
3. 请求失败,  显示`错误`缺省页

```kotlin hl_lines="2"
page.onRefresh {
    scope {
        // 请求到数据设置到RecyclerView
        rv_push.models = Get<Game>(Api.PATH).await().data.list
    }
}.autoRefresh()
```

## 生命周期

| 生命周期 | 描述                                               |
| -------- | -------------------------------------------------- |
| 开始     | `showLoading/autoRefresh`触发`onRefresh`, 开始请求 |
| 结束     | PageRefreshLayout被销毁, 请求自动取消              |