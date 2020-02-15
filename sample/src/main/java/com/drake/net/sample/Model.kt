package com.drake.net.sample

import androidx.lifecycle.SavedStateHandle
import com.drake.net.utils.SavedViewModel

class Model(saved: SavedStateHandle) : SavedViewModel(saved) {
    var name: String?
        get() = saved.get("name")
        set(value) {
            saved["name"] = value
        }
}