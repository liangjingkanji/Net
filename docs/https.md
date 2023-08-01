Net可快速配置Https证书, 或者使用OkHttp的方式

## 使用CA证书

Https如果使用的CA证书, 不需要任何配置可以直接访问

```kotlin
scopeNetLife {
    tvFragment.text = Get<String>("https://github.com/liangjingkanji/Net/").await()
}
```

## 信任所有证书

信任所有证书可以解决无法访问私有证书的Https地址问题

=== "全局配置"

    ```kotlin
    NetConfig.initialize(Api.HOST, this){
        trustSSLCertificate() // 信任所有证书
    }
    ```
=== "单例配置"

    ```kotlin
    scopeNetLife {
        Get<String>("https://github.com/liangjingkanji/Net/"){
          setClient {
              trustSSLCertificate()
          }
        }.await()
    }
    ```

## 导入证书

私有证书可以放到任何位置, 只要读取到`InputStream`流对象即可

=== "全局配置"

    ```kotlin
    NetConfig.initialize(Api.HOST, this) {
        val privateCertificate = resources.assets.open("https.certificate")
        setSSLCertificate(privateCertificate)
    }
    ```

=== "单例配置"

    ```kotlin
    scopeNetLife {
        Get<String>("https://github.com/liangjingkanji/Net/") {
            setClient {
                val privateCertificate = resources.assets.open("https.certificate") // 这里的证书是放到应用的资产目录下
                setSSLCertificate(privateCertificate)
            }
        }.await()
    }
    ```






