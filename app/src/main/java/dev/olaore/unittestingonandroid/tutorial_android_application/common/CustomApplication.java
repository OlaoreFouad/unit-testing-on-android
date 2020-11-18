package dev.olaore.unittestingonandroid.tutorial_android_application.common;

import android.app.Application;

import dev.olaore.unittestingonandroid.tutorial_android_application.common.dependencyinjection.CompositionRoot;

public class CustomApplication extends Application {

    private CompositionRoot mCompositionRoot;

    @Override
    public void onCreate() {
        super.onCreate();
        mCompositionRoot = new CompositionRoot();
    }

    public CompositionRoot getCompositionRoot() {
        return mCompositionRoot;
    }
}
