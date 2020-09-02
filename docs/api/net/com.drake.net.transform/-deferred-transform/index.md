[net](../../index.md) / [com.drake.net.transform](../index.md) / [DeferredTransform](./index.md)

# DeferredTransform

`data class DeferredTransform<T>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DeferredTransform(deferred: Deferred<T>, block: (T) -> T = { it })` |

### Properties

| Name | Summary |
|---|---|
| [block](block.md) | `val block: (T) -> T` |
| [deferred](deferred.md) | `val deferred: Deferred<T>` |
