package dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.views;

public interface ObservableViewMvc<ListenerType> extends ViewMvc {

    void registerListener(ListenerType listener);

    void unregisterListener(ListenerType listener);
}
