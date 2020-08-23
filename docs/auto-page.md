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

`index` 属于PageRefreshLayout的字段, 每次上拉加载自动+1递增, 下拉刷新自动重置

`addData` 属于PageRefreshLayout的函数

1. 函数接收数据集合作为第一个参数,
1. 第二个参数lambda为返回布尔类型是否为空页面判断(默认为集合数据`isEmpty`来判断),
1. 第三个参数lambda为返回布尔类型判断是否存在下一页(默认返回`true`).