Https访问主要就是证书配置问题, Net可以使用OkHttp一切函数组件, 且简化证书配置流程

> OkHttp如何配置证书, Net就可以如何配置证书

## 使用CA证书

Https如果是使用的CA颁发的证书, 不需要任何配置Net可以直接访问

```kotlin
scopeNetLife {
    tvFragment.text = Get<String>("https://github.com/").await()
}
```

## 信任所有证书

信任所有证书可以解决无法访问私有证书的Https地址问题, 但是这么做就失去了使用证书的含义, 不建议此做法

=== "全局配置"

    ```kotlin
    NetConfig.init("https://www.google.com/"){
        trustSSLCertificate() // 信任所有证书
    }
    ```
=== "单例配置"

    ```kotlin
    scopeNetLife {
        Get<String>("https://github.com/"){
          setClient {
              trustSSLCertificate()
          }
        }.await()
    }
    ```

## 导入证书

私有证书可以放到任何位置, 只要能读取到`InputStream`流对象即可

=== "全局配置"

    ```kotlin
    NetConfig.init("http://github.com/") {
        val privateCertificate = resources.assets.open("https.certificate")
        setSSLCertificate(privateCertificate)
    }
    ```

=== "单例配置"

    ```kotlin
    scopeNetLife {
        Get<String>("https://github.com/") {
            setClient {
                val privateCertificate = resources.assets.open("https.certificate") // 这里的证书是放到应用的资产目录下
                setSSLCertificate(privateCertificate)
            }
        }.await()
    }
    ```






