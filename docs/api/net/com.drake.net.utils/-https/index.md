[net](../../index.md) / [com.drake.net.utils](../index.md) / [Https](./index.md)

# Https

`object Https`

### Properties

| Name | Summary |
|---|---|
| [UnSafeHostnameVerifier](-un-safe-hostname-verifier.md) | 此类是用于主机名验证的基接口。 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配， 则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。策略可以是基于证书的或依赖于其他验证方案。 当验证 URL 主机名使用的默认规则失败时使用这些回调。如果主机名是可接受的，则返回 true`var UnSafeHostnameVerifier: `[`HostnameVerifier`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/HostnameVerifier.html) |
| [UnSafeTrustManager](-un-safe-trust-manager.md) | 为了解决客户端不信任服务器数字证书的问题，网络上大部分的解决方案都是让客户端不对证书做任何检查， 这是一种有很大安全漏洞的办法`var UnSafeTrustManager: `[`X509TrustManager`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/X509TrustManager.html) |
