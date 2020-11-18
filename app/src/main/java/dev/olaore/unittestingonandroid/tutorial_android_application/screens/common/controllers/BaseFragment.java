package dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.controllers;


import androidx.fragment.app.Fragment;

import dev.olaore.unittestingonandroid.tutorial_android_application.common.CustomApplication;
import dev.olaore.unittestingonandroid.tutorial_android_application.common.dependencyinjection.ControllerCompositionRoot;

public class BaseFragment extends Fragment {

    private ControllerCompositionRoot mControllerCompositionRoot;

    protected ControllerCompositionRoot getCompositionRoot() {
        if (mControllerCompositionRoot == null) {
            mControllerCompositionRoot = new ControllerCompositionRoot(
                    ((CustomApplication) requireActivity().getApplication()).getCompositionRoot(),
                    requireActivity()
            );
        }
        return mControllerCompositionRoot;
    }
}
