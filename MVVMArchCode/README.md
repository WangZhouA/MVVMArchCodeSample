# 获取源代码
通常来说，创建完你自己的项目后，使用 git init 让项目被 git 管理起来，.gitignore 文件可使用本框架的，然后执行你的第一次 commit 后，来看看如何在你的项目中使用本框架。

框架采用 Git 子模块的方式被你的项目引用，即 git 中 submodule 概念。

使用命令行进入要引入框架的目录，即你的**项目仓库根目录**，执行以下命令：

```
git submodule add https://xxxxx
```

后缀是此仓库的地址。比如你的项目名称为：MVVMArchCodeSample，那么在执行以上命令后，项目结构为：

```
MVVMArchCodeSample
    | app
    | gradle
    | ....
    | MVVMArchCode
    | ....
    | build.gradle
    | ....
```

如上所示说明已将框架作为子模块引入，引入的子模块是一个独立的仓库，不懂 submodule 概念的请自行搜索学习。
**采用 submodule 的好处是，后续若本框架有更新，框架可单独更新，跟正常的仓库更新没什么不一样，比如拉取最新的更新**，如下：

```
git pull origin master
```

框架引入后，你的项目仓库是父仓库，会出现两个文件：.gitmodules，MVVMArchCode，这两个文件是要被纳入父仓库中管理起来的，每次更新了框架的代码，MVVMArchCode 文件将会变化，该变化需要被仓库管理起来。

具体这两个文件是什么作用，请自行查阅 git 子模块是如何使用的。

**如果你在使用过程中发现框架有任何问题，可联系框架维护者，或者将你认为正确的修改提交到 dev 分支，并发起合并请求！！**

现在，整个框架的源码都拷贝下来了，下面我们进行一些基本的配置，就可以使用框架了。

# 基本配置
* 1、在项目的 settings.gradle 中加入框架：

```groovy
include ':MVVMArchCode'
```

* 2、项目根目录下的 build.gradle 引用 dependencies.gradle 文件（如下）

```groovy
buildscript {
    apply from: 'MVVMArchCode/dependencies.gradle'

    addRepository(repositories)

    dependencies {
        // 根据 AS 的版本而不同，注意，sample 需要在 3.4.2 及之下，否则 bindingAdapter 可能有问题 
        classpath 'com.android.tools.build:gradle:3.4.2'
    }
}

allprojects {
    addRepository(repositories)
}

configurations.all {
    resolutionStrategy {
        // 有需要的话，这里可以强制使用某个版本的库
        force 'androidx.annotation:annotation:1.1.0',
            'androidx.arch.core:core-common:2.0.1',
            ....
    }
}

...

```

其中 addRepository 会使用 阿里云 的 maven 仓库加快远程仓库的下载速度，**如果你发现有问题，请联系我们**。


* 3、模块（比如 app）目录下的 build.gradle 必须启用如下功能，并引用 MVVMArchCode 模块

```groovy
android {
    // TODO sdk 版本可动态配置，下面会说明
    compileSdkVersion SysConfig.compileSdkVersion

    defaultConfig {
        applicationId "com.xxxx.xxxxx"

        minSdkVersion SysConfig.minSdkVersion
        targetSdkVersion SysConfig.targetSdkVersion

        ...
    }

    // AS 4.0 及以上
    buildFeatures{
        dataBinding = true
    }

    // AS 4.0 以下
    dataBinding {
        enabled = true
    }
    
    // 以上 DataBinding 配置二选一

    // 必须开启 Java8
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
dependencies {
    ...

    /*
    初始将 appcompat，core-ktx，constraintlayout，kotlin 等默认添加的去掉，只留下 test 和 MVVMArchCode 即可。
    后续有其他需要的库请自行在项目中添加。
    */
    implementation project(path: ':MVVMArchCode')

    ...
}
```


* 4、生成配置文件，指定 SDK 版本，开启相关需要的库。
拷贝 MVVMArchCode/MVVMArchCode-config.groovy.template 到项目根目录，重命名为 mvvm-config.groovy。
配置 SDKVersion，include 指定你需要包含哪些库。

```groovy
include {
    // 内存泄露，默认只在 debug 期间生效
    leakCanary2=true
    // 应用前后台监听
    lifecycleProcess=true

    ...
}

SDKVersion {
    compileSdkVersion=29
    minSdkVersion=21
    targetSdkVersion=29
}
```

**配置了某个库为 false，就不能使用该库了，因为框架不会引入该库，会报找不到类异常**，具体是什么库，大部分见名知意，如果还不懂是什么库，可查看对应的 MVVMArchCode/dependencies.gradle 文件。

* 5、sync
此时基本配置已经完成了。**基本的使用可参照 MVVMHabit**

# Sample
详见 [MVVMArchitecture/code/MVVMArchCodeSample](http://10.38.16.38:7080/Mini_Game/MVVMArchitecture)，代码是基于 [MVVMHabit](https://github.com/goldze/MVVMHabit) 的 androidx 分支改造而来的。
已更新到最新的提交：解决日志拦截器缺陷，id 为 f52f1303df965c9bde77d7d9cf13b5dc3fcaada5，后续 MVVMHabit 有 Bug 修正，可手动同步过来，若大规模的更改，需酌情考虑是否同步。

# MVVMHabit - ReadMe
基础的使用和 [MVVMHabit](https://github.com/goldze/MVVMHabit) 差别不大，可以查考它的 ReadMe，如果发现有出入，而你不懂的话，可以联系框架维护者。


# 推荐的库和功能
在配置文件中可配置的库都是有经过框架二次封装的，其他的未封装的库，如果有需要，在项目中自行引入，详见 MVVMArchCode/dependencies.gradle 文件。

## 1. 列表，rv，vp，vp2 绑定库
```groovy
// 绑定库
implementation Deps.bindingAdapter
implementation Deps.bindingAdapterRv
implementation Deps.bindingAdapterVp2
```

具体详见 [binding-collection-adapter](https://github.com/evant/binding-collection-adapter)

## 2. 局域网浏览器可以看到数据库
```groovy
// 可以在浏览器中看到数据库，http://ip:8080
debugImplementation Deps.debugDB
```

如果你是 AS 4.1，那么使用 Database Inspector 即可。


## 3. 数据库
推荐使用 room，不要使用其他的，如果有使用，开启 mvvm-config.groovy 的 room 属性，然后在项目 build.gradle 中加入：
```groovy
annotationProcessor Deps.roomCompiler
```

框架提供了 RoomUtil 工具类用于获取数据库的实例。

## 4. 图片加载
使用的是 Glide， 开启 mvvm-config.groovy 的 glide 属性，在 xml 中即可如下使用：
```xml
<ImageView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    url="@{viewModel.mImageUrl}"
/>
```

还有占位符 placeholderRes 属性，具体原理详见 me.goldze.mvvmhabit.binding.viewadapter.image.ViewAdapter

## 5. 应用前后台监听
开启 mvvm-config.groovy 的 lifecycleProcess 属性，使用 AppStateTracker 类

## 6. 全局 Activity 管理
详见 AppManager 类，有些奇葩的手机，可能在当前页面横竖屏后，会导致上一个页面重建，导致 Activity 堆栈混乱，可能 currentActivity 不对，此时推荐使用 getActivity 获取具体实例。

## 7. 全局功能配置类
详见 GlobalConfig 及注释，可配置的功能部分局部可配，比如点击事件的时间间隔。

## 8. 各种 binding 类
详见 me.goldze.mvvmhabit.binding 包，定义许多的功能绑定，不保证一定符合你的需求，有需求可以提出来大家讨论后加到框架中去。

## 9.  网络请求
使用 Retrofit，提供 RetrofitClientUtil 工具类，可快速设置 baseURL，设置通用请求头，获取实例，此处方案也许不是最佳的，根据实际使用情况再看是否重新优化。


# 关于设置点击事件间隔时间的问题
通常为了防止重复点击，我们需要设置多次点击同一个控件的间隔时间。
使用 android:onClick 属性设置点击事件是不会延迟点击事件的，点击了多少就响应多少。
使用 onClickCommand="@{viewModel.xxxx}" 属性设置的点击事件，可灵活设置是否让两次点击事件有间隔的时间，防止重复点击。规则如下：

1. 如果你需要开启这个功能，首先得配置 mvvm-config.groovy 文件的 rxBinding 属性为 true，这样框架才会引入 rxBinding 库实现点击事件间隔。否则不开启这个功能。
2. 如果你只有局部需要点击事件间隔，可通过 GlobalConfig 的 mIsClickInterval 属性全局开启或关闭，此属性默认和第 1 点的 rxBinding 属性保持一致，还有 mClickIntervalMilliseconds 可全局设置间隔时间。
然后单独在你的 xml 中使用 isInterval 和 intervalMilliseconds 属性开启或关闭此功能，可自定义间隔的时间，毫秒为单位。
3. 如果你的 rxBinding 设置为 false，而 GlobalConfig 的 mIsClickInterval 又手动设置为 true，将报找不到类异常。


# 可能遇到的问题

## 1. BR 的问题
有些时候 BR 类不会自动导入，需要你手动 import，甚至是导入后还是报红色，但其实已经可以运行了，这是 AS 的锅。

## 2. Room 数据库
```
Caused by: java.lang.RuntimeException: cannot find implementation for com.xxx.xxx.xx.Xxxx Xxxx_Impl does not exist
```

就是说没有生成 Impl 类。解决方案如下：

Java 需要开启：
```groovy
dependencies {
    annotationProcessor Deps.roomCompiler
}
```

Kotlin 需要开启 kapt，并在 build.gradle 中加入：
```groovy
dependencies {
    kapt Deps.roomCompiler
}
```

## 3. 引用冲突
```
Unable to resolve dependency for ':MVVMArchitecture@debug/compileClasspath': Could not resolve xx.xxx.xxx:xxx:x.x.x.
```

发生上述冲突的原因是因为框架有库以 api 和 compileOnly 两种方式引用了。
举个例子做个假设，roomRxJava 会引入 RxJava，而 retrofit2RxJava2 也会引入 RxJava，如果框架中的 build.gradle 文件按照以下来引用：

```groovy
dependencies {
    if (false) {
        api Deps.roomRuntime
        api Deps.roomRxJava
        kapt Deps.roomCompiler
    } else {
        compileOnly Deps.roomRuntime
        compileOnly Deps.roomRxJava
    }

    if (true) {
        api Deps.retrofit2RxJava2
    }
}
```

compileOnly 是为了框架中有用到 room 的代码不要编译报错。以上是没有开启 room，只开启了 retrofit2RxJava2，那么就会报错，因为 RxJava 在上面的 else 中已经是 compileOnly 的了，下面的 if 却是 api 的。

解决方法是：
1. 因为框架没有代码用到 roomRxJava，所以可以不用 compileOnly。
2. 如果用到了，就得想办法根据实际情况进行 exclude 了。

## 4. MVVMArchitecture/mvvm-config.groovy.template 和项目目录下的 mvvm-config.groovy 的冲突
```
Caused by: java.io.NotSerializableException: groovy.util.ConfigObject
```

两个文件的属性没有同步，比如根目录下的文件少了某个属性。已增加检测属性的功能，如果 gradle sync 失败，根据提示操作即可。

## 5. 关于 DataBinding 自定义 BindingAdapter 的问题
这一块是属于 DataBinding 的知识，但是 MVVMHabit 的用法有误，导致有同学提出异议，这里做下解释。

首先如下的定义是会生成自定义属性的：

```java
@BindingAdapter(value = {"onClickCommand", "isInterval", "intervalMilliseconds"}, requireAll = false)
public static void onClickCommand(View view, final BindingCommand clickCommand, Boolean isInterval, Integer intervalMilliseconds) {
}
```

在 xml 中使用如下：

```xml
<Button
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="登录"
    isInterval="@{false}"
    intervalMilliseconds="@{900}"
    onClickCommand="@{viewModel.loginOnClickCommand}" />
```

因为定义 BindingAdapter 时，属性是没有前缀的，而有些同学这么使用：

```xml
<Button
    xmlns:binding="http://schemas.android.com/apk/res-auto"
    binding:isInterval="@{false}"
    />
```

这样是不对的，按照上述的使用，意思其实是 Button 这个类在自定义 View 时自定义了一个属性 isInterval，而命名空间 binding 会帮这个类自动找到该属性，但 DataBinding 不是自定义 View，所以上述使用在最新的框架使用中是会报错的。

