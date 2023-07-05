Net支持请求返回的数据类型取决于你自己的转换器实现(即理论上支持返回任何对象):

# Get<任何对象>("path").await()

默认情况下支持返回以下数据类型

| 函数 | 描述 |
|-|-|
| String | 字符串 |
| ByteArray | 字节数组 |
| ByteString | 内部定义的一种字符串对象 |
| File | 文件对象, 这种情况其实应当称为[下载文件](download-file.md) |
| Response | 最基础的, 包含全部响应信息的对象(响应体/响应头/请求信息等) |

使用示例

```kotlin
scopeNetLife {
    Get<Response>("api").await().headers("响应头名称") // 返回响应头
}
```

??? summary "默认使用的是: [NetConverter.DEFAULT](https://github.com/liangjingkanji/Net/blob/master/net/src/main/java/com/drake/net/convert/NetConverter.kt)"
    ```kotlin
    interface NetConverter {

        @Throws(Throwable::class)
        fun <R> onConvert(succeed: Type, response: Response): R?

        companion object DEFAULT : NetConverter {
            /**
             * 返回结果应当等于泛型对象, 可空
             * @param succeed 请求要求返回的泛型类型
             * @param response 请求响应对象
             */
            override fun <R> onConvert(succeed: Type, response: Response): R? {
                return when {
                    succeed === String::class.java && response.isSuccessful -> response.body?.string() as R
                    succeed === ByteString::class.java && response.isSuccessful -> response.body?.byteString() as R
                    succeed is GenericArrayType && succeed.genericComponentType === Byte::class.java && response.isSuccessful -> response.body?.bytes() as R
                    succeed === File::class.java && response.isSuccessful -> response.file() as R
                    succeed === Response::class.java -> response as R
                    else -> throw ConvertException(response, "An exception occurred while converting the NetConverter.DEFAULT")
                }
            }
        }
    }
    ```

假设这里没有你需要的数据类型请[自定义转换器](/converter/#_3)(例如返回Json或Protocol)