Net属于低耦合框架, 自动下拉刷新需要依赖第三方组件: [BRV](https://github.com/liangjingkanji/BRV)(点击链接按文档依赖)
<br>

<a href="https://jitpack.io/#liangjingkanji/BRV"><img src="https://jitpack.io/v/liangjingkanji/BRV.svg"/></a>

使用固定版本号替换+符号

```groovy
implementation 'com.github.liangjingkanji:BRV:+'
```

> 当然如果自己处理下拉刷新也是可以的, Net可以仅仅作为网络框架存在

创建PageRefreshLayout
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

创建列表
```kotlin
rv_push.linear().setup {
    addType<String>(R.layout.item_list)
}
```

创建网络请求
```kotlin hl_lines="2"
page.onRefresh {
    scope {
        // 请求到数据设置到RecyclerView
        rv_push.models = Get<ListModel>("list").await().data.list
    }
}.autoRefresh()
```

<br>

> 注意高亮处使用的是`scope`而不是其他作用域, 只能使用scope, 否则无法跟随PageRefreshLayout生命周期等功能

<br>

- 使用上和自动缺省页相似
- BRV同样属于具备完善功能独立的RecyclerView框架
- BRV的下拉刷新扩展自[SmartRefreshLayout_v2](https://github.com/scwang90/SmartRefreshLayout), 支持其全部功能且更多

## 生命周期

|生命周期|描述|
|-|-|
|开始|PageRefreshLayout执行`showLoading/autoRefresh`后触发`onRefresh`, 然后开始网络请求|
|结束|PageRefreshLayout被销毁(例如其所在的Activity或Fragment被销毁), 网络请求自动取消|