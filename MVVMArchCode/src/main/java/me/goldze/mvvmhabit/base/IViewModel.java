package me.goldze.mvvmhabit.base;


import androidx.lifecycle.DefaultLifecycleObserver;

/**
 * Created by goldze on 2017/6/15.
 */

public interface IViewModel extends DefaultLifecycleObserver {
    /**
     * 注册RxBus
     */
    void registerRxBus();

    /**
     * 移除RxBus
     */
    void removeRxBus();
}
