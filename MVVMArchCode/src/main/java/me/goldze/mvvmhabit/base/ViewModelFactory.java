package me.goldze.mvvmhabit.base;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by goldze on 2018/9/30.
 */

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }


    private ViewModelFactory(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BaseViewModel.class)) {
            //noinspection unchecked
            return (T) new BaseViewModel(mApplication);
        }
        //反射动态实例化ViewModel
        try {
            String className = modelClass.getCanonicalName();
            if (className == null) {
                throw new RuntimeException("className is null");
            }
            Class<?> classViewModel = Class.forName(className);
            Constructor<?> cons = classViewModel.getConstructor(Application.class);
            ViewModel viewModel = (ViewModel) cons.newInstance(mApplication);
            //noinspection unchecked
            return (T) viewModel;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}
