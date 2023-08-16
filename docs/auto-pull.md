首先请阅读上章节 [自动下拉刷新](auto-refresh.md), 已提及内容不再重复


## 自动分页

提供`addData()`来简化分页, 开发者可以借鉴实现

```kotlin
page.onRefresh {
    scope {
        val data = Get<Game>(Api.PATH) {
            param("page", index)
        }.await().data
        addData(data.list) {
            index < data.total
        }
    }
}.autoRefresh()
```

## 索引自增
`index` 每次上拉加载自动++1, 刷新列表重置为`PageRefreshLayout.startIndex`

## 有更多页

根据`hasMore`返回结果是否关闭上拉加载, `isEmpty`决定是否显示`空数据`缺省页

```kotlin
fun addData(
    data: List<Any?>?,
    adapter: BindingAdapter? = null,
    isEmpty: () -> Boolean = { data.isNullOrEmpty() },
    hasMore: BindingAdapter.() -> Boolean = { true }
)
```

<br>
1. [PageRefreshLayout 下拉刷新/上拉加载](https://liangjingkanji.github.io/BRV/refresh.html)