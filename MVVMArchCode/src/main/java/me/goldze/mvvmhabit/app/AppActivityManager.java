package me.goldze.mvvmhabit.app;

import android.app.Activity;

import java.util.Stack;

/**
 * activity堆栈式管理
 */
public class AppActivityManager {

    private static Stack<Activity> sActivityStack = new Stack<>();

    private AppActivityManager() {
    }

    private static class SingleHolder {
        private static final AppActivityManager INSTANCE = new AppActivityManager();
    }

    public static AppActivityManager getAppManager() {
        return SingleHolder.INSTANCE;
    }

    public static Stack<Activity> getActivityStack() {
        return sActivityStack;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        sActivityStack.add(activity);
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            sActivityStack.remove(activity);
        }
    }


    /**
     * 是否有activity
     */
    public boolean isActivity() {
        return !sActivityStack.isEmpty();
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (sActivityStack.isEmpty()) {
            return null;
        }
        return sActivityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        finishActivity(currentActivity());
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : sActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = sActivityStack.size(); i < size; i++) {
            if (null != sActivityStack.get(i)) {
                finishActivity(sActivityStack.get(i));
            }
        }
        sActivityStack.clear();
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : sActivityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
//          android.os.Process.killProcess(android.os.Process.myPid());
//            调用 System.exit(n) 实际上等效于调用：
//            Runtime.getRuntime().exit(n)
//            finish()是Activity的类方法，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，退出当前Activity并释放资源（内存），但是该方法不可以结束整个App如有多个Activty或者有其他组件service等不会结束。
//            其实android的机制决定了用户无法完全退出应用，当你的application最长时间没有被用过的时候，android自身会决定将application关闭了。
            //System.exit(0);
        } catch (Exception e) {
            sActivityStack.clear();
            e.printStackTrace();
        }
    }
}