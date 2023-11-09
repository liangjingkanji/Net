package com.drake.net.sample.constants

import com.drake.serialize.serialize.annotation.SerializeConfig
import com.drake.serialize.serialize.serialLazy


/**
 * 本单例类使用 https://github.com/liangjingkanji/Serialize 为字段提供持久化存储
 */
@SerializeConfig(mmapID = "user_config")
object UserConfig {

    var token by serialLazy(default = "6cad0ff06f5a214b9cfdf2a4a7c432339")

    var isLogin by serialLazy(default = false)
}