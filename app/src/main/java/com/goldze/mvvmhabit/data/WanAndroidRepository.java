package com.goldze.mvvmhabit.data;

import com.goldze.mvvmhabit.data.source.WanAndroidHttpDataSource;
import com.goldze.mvvmhabit.data.source.WanAndroidLocalDataSource;

import me.goldze.mvvmhabit.base.BaseModel;

public class WanAndroidRepository extends BaseModel implements WanAndroidHttpDataSource, WanAndroidLocalDataSource {
}
