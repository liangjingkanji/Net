阅读自动分页加载之前请先阅读自动刷新

Net属于低耦合框架, 分页加载同样需要依赖第三方组件: [BRV](https://github.com/liangjingkanji/BRV)(点击链接按文档依赖)


创建布局
```xml
<com.drake.brv.PageRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/page"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.fragment.PullRefreshFragment">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rv_pull"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</com.drake.brv.PageRefreshLayout>
```

创建列表
```kotlin
rv_pull.linear().setup {
    addType<String>(R.layout.item_list)
}
```

创建网络
```kotlin
page.onRefresh {
    scope {
        val data = Get<ListModel>("list") {
            param("page", index)
        }.await().data
        addData(data.list) {
            index < data.total
        }
    }
}.autoRefresh()
```

- `index` 属于PageRefreshLayout的字段, 每次上拉加载自动+1递增, 下拉刷新自动重置
- ` data.total`属于服务器返回的`列表全部数量`的字段, 最终使用什么字段或者判断条件请自己根据项目不同决定
- `addData` 属于PageRefreshLayout的函数
    ```kotlin
    fun addData(
        data: List<Any?>?,
        adapter: BindingAdapter? = null,
        isEmpty: () -> Boolean = { data.isNullOrEmpty() },
        hasMore: BindingAdapter.() -> Boolean = { true }
    )
    ```
    具体请查看函数注释