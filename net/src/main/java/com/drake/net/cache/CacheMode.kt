package com.drake.net.cache

enum class CacheMode {

    /** 只读取缓存, 本操作并不会请求网络故不存在写入缓存 */
    READ,

    /** 只请求网络, 强制写入缓存 */
    WRITE,

    /** 先从缓存读取，如果失败再从网络读取, 强制写入缓存 */
    READ_THEN_REQUEST,

    /** 先从网络读取，如果失败再从缓存读取, 强制写入缓存 */
    REQUEST_THEN_READ,
}