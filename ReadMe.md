# 如何运行
整个 MVVMArchitecture 仓库克隆下来后，或者已有这个仓库了，更新代码到最新后，code/MVVMArchCodeSample/MVVMArchCode 是空目录，因为 MVVMArchCode 是子模块，即 git 的 submodule，它也是一个独立的仓库。

需要将子模块的仓库也一起克隆下来，有以下方式可以做到：

## 1、克隆时把子模块也一起克隆
执行以下命令：

```
git clone --recursive xxx仓库地址xxx
```

## 2、 已有仓库初始化子模块
执行以下命令：

```
git submodule init
git submodule update --remote
```

即可从远程仓库拉取最新的 MVVMArchCode 代码。初始化成功后，MVVMArchCode 的后续更新就跟普通仓库一样了。

---

如果对于 git 的 submodule 概念不熟悉，请自行学习。

如果其他新的项目需要使用 MVVMArchCode 框架，请参阅 [MVVMArchCode](http://10.38.16.38:7080/Mini_Game/MVVMArchCode) 项目的 ReadMe