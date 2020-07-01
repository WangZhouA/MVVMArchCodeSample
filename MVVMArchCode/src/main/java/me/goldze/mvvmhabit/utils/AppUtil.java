package me.goldze.mvvmhabit.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseApplication;

public final class AppUtil {
    private static final String TAG = "AppUtil";

    private AppUtil() {
    }

    public static Intent shareTextIntent(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    public static List<ResolveInfo> getTextReceivers(PackageManager pm) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        final List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfos;
    }

    /**
     * @return 获取所有的带启动图标的App
     */
    public static List<ResolveInfo> getLaunchableResolveInfos() {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return BaseApplication.getInstance().getPackageManager().queryIntentActivities(intent, 0);
    }

    /**
     * 取得版本名称，对应 AndroidManifest.xml 文件 <manifest> 标签下的 versionName 属性
     *
     * @return 返回版本字符串，比如：1.0
     */
    public static String getVersionName() {
        return getDefPackageInfo().versionName;
    }

    /**
     * 取得版本号，对应 AndroidManifest.xml 文件 <manifest> 标签下的 versionCode 属性
     *
     * @return 返回版本，比如：1
     */
    public static int getVersionCode() {
        return getDefPackageInfo().versionCode;
    }

    public static PackageInfo getDefPackageInfo() {
        PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }

    public static boolean isAppExist(String packageName) {
        final PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void gotoAppDetailsSettings(Context context, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
    }

    public static String getCurProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) BaseApplication.getInstance().getSystemService(
                Context.ACTIVITY_SERVICE);
        if (mActivityManager != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return null;
    }

    public static String getAppLabel() {
        PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
        try {
            return packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(BaseApplication.getInstance().getPackageName(),
                            0)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean startActivityForPackage(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (null != intent) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 打开其他的Activity
     *
     * @param cls 要打开的Activity
     */
    public static void startActivity(Context context, Class<?> cls) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, cls));
    }

    /**
     * 打开其他的Activity，并附带Object
     *
     * @param cls   要打开的Activity
     * @param value 要附带的Object
     */
    //    public static void startActivity(Context context, Class<?> cls, String name, Object value)
    //    {
    //        Intent intent = new Intent(context, cls);
    //        intent.putExtra(name, PG.convertParcelable(value));
    //        context.startActivity(intent);
    //    }
    //
    //    public static void startActivity(Context context, Class<?> cls, Object value)
    //    {
    //        Intent intent = new Intent(context, cls);
    //        intent.putExtra(Constants.EXTRA_PARCELABLE, PG.convertParcelable(value));
    //        context.startActivity(intent);
    //    }
    //
    //    public static void startActivity(Context context, Intent intent, String name, Object value)
    //    {
    //        intent.putExtra(name, PG.convertParcelable(value));
    //        context.startActivity(intent);
    //    }
    public static void startActivity(Context context, Class<?> cls, String name, int value) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(name, value);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> cls, String name, boolean value) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(name, value);
        context.startActivity(intent);
    }

    /**
     * 打开其他的Activity，并附带字符串
     *
     * @param cls   要打开的Activity
     * @param name  字符串名称
     * @param value 字符串值
     */
    public static void startActivity(Context context, Class<?> cls, String name, String value) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(name, value);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode,
                                              String name, String value) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(name, value);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 检测 响应某个意图的Activity 是否存在
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    public static boolean isClickInterval() {
        try {
            ApplicationInfo info = BaseApplication.getInstance().getPackageManager().getApplicationInfo(BaseApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getBoolean("isClickInterval");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
