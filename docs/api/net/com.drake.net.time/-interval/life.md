[net](../../index.md) / [com.drake.net.time](../index.md) / [Interval](index.md) / [life](./life.md)

# life

`fun life(lifecycleOwner: LifecycleOwner, lifeEvent: Event = Lifecycle.Event.ON_STOP): `[`Interval`](index.md)

绑定生命周期, 在指定生命周期发生时取消轮循器

### Parameters

`lifecycleOwner` - 默认在销毁时取消轮循器