package me.goldze.mvvmhabit.binding.viewadapter.checkbox;

import android.widget.CheckBox;

import androidx.databinding.BindingAdapter;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Created by goldze on 2017/6/16.
 */

public class ViewAdapter {
    /**
     * @param bindingCommand //绑定监听
     */
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void setCheckedChanged(final CheckBox checkBox, final BindingCommand<Boolean> bindingCommand) {
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> bindingCommand.execute(b));
    }
}
