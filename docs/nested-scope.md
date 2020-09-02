有时候可能面临嵌套的`scope*`函数或者作用域内有子作用域情况, 这个时候的生命周期是如何


## 嵌套Scope

```kotlin hl_lines="5"
scopeNet {
    val task = Post<String>("api0").await()

    scopeNet {
        val task = Post<String>("api0").await() // 此时发生请求错误
    }.catch {
        // A
    }
}.catch {
    // B
}
```

- 以下嵌套作用域错误将会仅发生在`A`处, 并被捕获, 同时不影响外部`scopeNet`的请求和异常捕获
- 两个`scopeNet`的异常抛出和捕获互不影响
- `scopeNet/scopeDialog/scope`等函数同理

## 子作用域

```kotlin hl_lines="7 10"
scopeNet {
    val await = Post<String>("api").await()

    launch {
       val task = Post<String>("api0").await()  // 此时发生请求错误
    }.invokeOnCompletion {
        // A
    }
}.catch {
     // B
}
```

- 这种情况 先执行`A`然后执行`B`, 并且都能捕获异常.
- 同时`scopeNet`发生错误也会导致`launch`内的请求被取消, `launch`发生错误也会导致`scopeNet`发生错误