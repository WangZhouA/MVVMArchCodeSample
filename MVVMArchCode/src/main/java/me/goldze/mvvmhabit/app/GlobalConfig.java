package me.goldze.mvvmhabit.app;

import me.goldze.mvvmhabit.utils.AppUtil;

/**
 * 全局配置类
 */
public final class GlobalConfig {
    private GlobalConfig() {
    }

    /**
     * 在 xml 配置点击事件，可配置的属性如下：
     * onClickCommand 点击事件
     * isInterval 是否开启防止点击过快
     * intervalMilliseconds 防止点击过快的间隔时间，毫秒为单位
     *
     * 这里可全局设置是否开启防止点击事件过快的功能，局部可单独开启或关闭。
     *
     * 如果关闭，那么和 setOnClickListener 没啥区别
     */
    public static boolean mIsClickInterval = AppUtil.isClickInterval();

    /**
     * 点击事件时间间隔
     */
    public static int mClickIntervalMilliseconds = 800;
}
