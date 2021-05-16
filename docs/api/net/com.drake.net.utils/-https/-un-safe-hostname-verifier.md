[net](../../index.md) / [com.drake.net.utils](../index.md) / [Https](index.md) / [UnSafeHostnameVerifier](./-un-safe-hostname-verifier.md)

# UnSafeHostnameVerifier

`var UnSafeHostnameVerifier: `[`HostnameVerifier`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/HostnameVerifier.html)

此类是用于主机名验证的基接口。 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，
则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。策略可以是基于证书的或依赖于其他验证方案。
当验证 URL 主机名使用的默认规则失败时使用这些回调。如果主机名是可接受的，则返回 true

