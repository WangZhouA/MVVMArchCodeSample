package me.goldze.mvvmhabit.app;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

/**
 * App 状态监听器，可用于判断应用是在后台还是在前台
 */
public class AppStateTracker {
    private static boolean mIsTract = false;

    private static AppStateChangeListener mChangeListener;

    public static final int STATE_FOREGROUND = 0;

    public static final int STATE_BACKGROUND = 1;

    private static int currentState = STATE_BACKGROUND;

    public static int getCurrentState() {
        if (!mIsTract) {
            throw new RuntimeException("必须先调用 track 方法");
        }
        return currentState;
    }

    public interface AppStateChangeListener {
        void appTurnIntoForeground();

        void appTurnIntoBackground();
    }

    public static void track(final AppStateChangeListener appStateChangeListener) {
        if (mIsTract) {
            return;
        }
        mIsTract = true;
        mChangeListener = appStateChangeListener;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifecycleChecker());
    }

    public static class LifecycleChecker implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        void onResume() {
            currentState = STATE_FOREGROUND;
            if (mChangeListener != null) {
                mChangeListener.appTurnIntoForeground();
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        void onPause() {
            currentState = STATE_BACKGROUND;
            if (mChangeListener != null) {
                mChangeListener.appTurnIntoBackground();
            }
        }
    }
}
