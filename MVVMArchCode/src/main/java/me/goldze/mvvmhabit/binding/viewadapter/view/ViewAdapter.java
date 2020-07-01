package me.goldze.mvvmhabit.binding.viewadapter.view;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.databinding.BindingAdapter;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import me.goldze.mvvmhabit.app.GlobalConfig;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Created by goldze on 2017/6/16.
 */

@SuppressWarnings("ResultOfMethodCallIgnored")
@SuppressLint("CheckResult")
public class ViewAdapter {
    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     * isInterval true 开启防止过快点击
     * intervalMilliseconds 间隔时间，毫秒
     */
    @BindingAdapter(value = {"onClickCommand", "isInterval", "intervalMilliseconds"}, requireAll = false)
    public static void onClickCommand(View view, final BindingCommand clickCommand, Boolean isInterval, Integer intervalMilliseconds) {
        // xml中没有配置，那么使用全局的配置
        if (isInterval == null) {
            isInterval = GlobalConfig.mIsClickInterval;
        }
        if (isInterval) {
            // 没有配置时间，使用全局配置
            if (intervalMilliseconds == null) {
                intervalMilliseconds = GlobalConfig.mClickIntervalMilliseconds;
            }
            RxView.clicks(view)
                    .throttleFirst(intervalMilliseconds, TimeUnit.MILLISECONDS)
                    .subscribe(object -> {
                        if (clickCommand != null) {
                            clickCommand.execute();
                            clickCommand.execute(view);
                        }
                    });
        } else {
            view.setOnClickListener(v -> {
                if (clickCommand != null) {
                    clickCommand.execute();
                    clickCommand.execute(view);
                }
            });
        }
    }

    /**
     * view的onLongClick事件绑定
     */
    @BindingAdapter(value = {"onLongClickCommand"}, requireAll = false)
    public static void onLongClickCommand(View view, final BindingCommand clickCommand) {
        RxView.longClicks(view)
                .subscribe(object -> {
                    if (clickCommand != null) {
                        clickCommand.execute();
                    }
                });
    }

    /**
     * 回调控件本身
     */
    @BindingAdapter(value = {"currentView"}, requireAll = false)
    public static void replyCurrentView(View currentView, BindingCommand<View> bindingCommand) {
        if (bindingCommand != null) {
            bindingCommand.execute(currentView);
        }
    }

    /**
     * view是否需要获取焦点
     */
    @BindingAdapter({"requestFocus"})
    public static void requestFocusCommand(View view, final Boolean needRequestFocus) {
        if (needRequestFocus) {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        } else {
            view.clearFocus();
        }
    }

    /**
     * view的焦点发生变化的事件绑定
     */
    @BindingAdapter({"onFocusChangeCommand"})
    public static void onFocusChangeCommand(View view, final BindingCommand<Boolean> onFocusChangeCommand) {
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (onFocusChangeCommand != null) {
                onFocusChangeCommand.execute(hasFocus);
            }
        });
    }

    /**
     * view的显示隐藏
     */
    @BindingAdapter(value = {"isVisible"}, requireAll = false)
    public static void isVisible(View view, final Boolean visibility) {
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
//    @BindingAdapter({"onTouchCommand"})
//    public static void onTouchCommand(View view, final ResponseCommand<MotionEvent, Boolean> onTouchCommand) {
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (onTouchCommand != null) {
//                    return onTouchCommand.execute(event);
//                }
//                return false;
//            }
//        });
//    }
}
