Net支持请求返回的数据类型取决于你的转换器实现

# Get<任何对象>("path").await()

默认转换器支持返回以下数据类型

| 函数 | 描述 |
|-|-|
| String | 字符串 |
| ByteArray | 字节数组 |
| ByteString | 更多功能的字符串对象 |
| File | 文件对象, 这种情况其实应当称为[下载文件](download-file.md) |
| Response | 所有响应信息(响应体/响应头/请求信息等) |

使用示例

```kotlin
scopeNetLife {
    Get<Response>("api").await().headers("响应头名称") // 返回响应头
}
```

??? example "转换器实现非常简单"
    ```kotlin title="NetConverter.kt" linenums="1"
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

假设不支持你需要的数据类型, 例如JSON/ProtoBuf/Bitmap等请[自定义转换器](converter-customize.md#_3)