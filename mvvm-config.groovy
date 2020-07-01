/**
 * 此配置文件决定需要开启哪些库的使用，有些库是有关联的，比如 bindingCollection 引用了 recyclerView。
 * 因此即使禁用掉 recyclerView，开启 bindingCollection，也会导入 recyclerView
 *
 * 禁用掉的库，就不能使用该库的内容，在 release 打包的时候会从包中移除。
 *
 * 只有框架封装过的库才需要动态化，比如 glide，因为在 BindingAdapter 中封装了 glide。
 * 否则不需要动态化，项目需要自己引入就可以了。
 */
include {
    // 内存泄露，默认只在 debug 期间生效
    leakCanary2=true
    // 应用前后台监听
    lifecycleProcess=true

    glide=true
    retrofit2=true
    room=true
    swiperefreshlayout=true
    recyclerView=true

    rxBinding=false
}

SDKVersion {
    compileSdkVersion=29
    minSdkVersion=21
    targetSdkVersion=29
}