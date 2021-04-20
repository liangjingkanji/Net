[net](../../index.md) / [com.drake.net.time](../index.md) / [Interval](index.md) / [life](./life.md)

# life

`fun life(lifecycleOwner: LifecycleOwner, lifeEvent: Event = Lifecycle.Event.ON_STOP): `[`Interval`](index.md)

绑定生命周期, 在指定生命周期发生时取消轮循器

### Parameters

`lifecycleOwner` - 默认在销毁时取消轮循器

`lifeEvent` -

销毁的时机, 默认为 ON_STOP 界面停止时停止轮循器



Fragment的显示/隐藏不会调用onDestroy, 故轮循器默认是在ON_STOP停止, 如果你设置ON_DESTORY请考虑Fragment的情况下

