[net](../../index.md) / [com.drake.net.okhttp](../index.md) / [okhttp3.OkHttpClient.Builder](index.md) / [setSSLCertificate](./set-s-s-l-certificate.md)

# setSSLCertificate

`fun Builder.setSSLCertificate(trustManager: `[`X509TrustManager`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/X509TrustManager.html)`?, bksFile: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`? = null, password: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): <ERROR CLASS>`

### Parameters

`trustManager` - 如果需要自己校验，那么可以自己实现相关校验，如果不需要自己校验，那么传null即可

`bksFile` - 客户端使用bks证书校验服务端证书

`password` - bks证书的密码`fun Builder.setSSLCertificate(vararg certificates: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`, bksFile: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`? = null, password: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): <ERROR CLASS>`

### Parameters

`certificates` - 含有服务端公钥的证书校验服务端证书

`bksFile` - 客户端使用bks证书校验服务端证书

`password` - bks证书的密码