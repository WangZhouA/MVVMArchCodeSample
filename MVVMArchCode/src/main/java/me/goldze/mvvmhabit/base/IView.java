package me.goldze.mvvmhabit.base;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by goldze on 2017/6/15.
 */

public interface IView {
    /**
     * 初始化界面传递参数
     */
    default void initParam() {}
    /**
     * 初始化数据
     */
    default void initData() {}

    /**
     * 初始化界面观察者的监听
     */
    default void initViewObservable() {}

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    int initVariableId();

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    int initContentView();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    default <VM> VM initViewModel(ViewModelStoreOwner owner) {
        Class modelClass;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
        } else {
            //如果没有指定泛型参数，则默认使用BaseViewModel
            modelClass = BaseViewModel.class;
        }
        //noinspection unchecked
        return (VM) new ViewModelProvider(owner).get(modelClass);
    }
}
