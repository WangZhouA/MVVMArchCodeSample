package com.goldze.mvvmhabit.data.source.http.service;

import com.goldze.mvvmhabit.entity.DemoEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.GET;

/**
 * Created by goldze on 2017/6/15.
 */

public interface WanAndroidApiService {
    @GET("wxarticle/chapters/json")
    Observable<BaseResponse<DemoEntity>> demoGet();
}
