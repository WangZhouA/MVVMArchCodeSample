package com.goldze.mvvmhabit.app;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.goldze.mvvmhabit.BuildConfig;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.ui.login.LoginActivity;

import me.goldze.mvvmhabit.app.GlobalConfig;
import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * Created by goldze on 2017/7/16.
 */

public class AppApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //是否开启打印日志
        KLog.init(BuildConfig.DEBUG);
        //初始化全局异常崩溃
        initCrash();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifecycleChecker());

        GlobalConfig.mIsClickInterval = false;
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(LoginActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }

    private static class LifecycleChecker implements LifecycleObserver {
        /**
         * 当关联的页面生命周期改变时回调
         */
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        void onAny(LifecycleOwner owner, Lifecycle.Event event) {
            Log.i("test", "onAny: "+owner+"\t"+event);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        private void onAppBackground() {
            // 应用进入后台
            Log.e("test","LifecycleChecker onAppBackground ON_STOP");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        private void onAppForeground() {
            // 应用进入前台
            Log.e("test","LifecycleChecker onAppForeground ON_START");

        }
    }
}
