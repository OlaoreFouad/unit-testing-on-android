package dev.olaore.unittestingonandroid.tutorial_android_application.common.dependencyinjection;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import dev.olaore.unittestingonandroid.tutorial_android_application.networking.StackoverflowApi;
import dev.olaore.unittestingonandroid.tutorial_android_application.networking.questions.FetchLastActiveQuestionsEndpoint;
import dev.olaore.unittestingonandroid.tutorial_android_application.networking.questions.FetchQuestionDetailsEndpoint;
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.FetchLastActiveQuestionsUseCase;
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.FetchQuestionDetailsUseCase;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.ViewMvcFactory;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.controllers.BackPressDispatcher;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.fragmentframehelper.FragmentFrameHelper;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.fragmentframehelper.FragmentFrameWrapper;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.navdrawer.NavDrawerHelper;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.screensnavigator.ScreensNavigator;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.toastshelper.ToastsHelper;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questiondetails.QuestionDetailsController;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist.QuestionsListController;

public class ControllerCompositionRoot {

    private final CompositionRoot mCompositionRoot;
    private final FragmentActivity mActivity;

    public ControllerCompositionRoot(CompositionRoot compositionRoot, FragmentActivity activity) {
        mCompositionRoot = compositionRoot;
        mActivity = activity;
    }

    private FragmentActivity getActivity() {
        return mActivity;
    }

    private Context getContext() {
        return mActivity;
    }

    private FragmentManager getFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    private StackoverflowApi getStackoverflowApi() {
        return mCompositionRoot.getStackoverflowApi();
    }

    private FetchLastActiveQuestionsEndpoint getFetchLastActiveQuestionsEndpoint() {
        return new FetchLastActiveQuestionsEndpoint(getStackoverflowApi());
    }

    private FetchQuestionDetailsEndpoint getFetchQuestionDetailsEndpoint() {
        return new FetchQuestionDetailsEndpoint(getStackoverflowApi());
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    public ViewMvcFactory getViewMvcFactory() {
        return new ViewMvcFactory(getLayoutInflater(), getNavDrawerHelper());
    }

    private NavDrawerHelper getNavDrawerHelper() {
        return (NavDrawerHelper) getActivity();
    }

    public FetchQuestionDetailsUseCase getFetchQuestionDetailsUseCase() {
        return new FetchQuestionDetailsUseCase(getFetchQuestionDetailsEndpoint());
    }

    public FetchLastActiveQuestionsUseCase getFetchLastActiveQuestionsUseCase() {
        return new FetchLastActiveQuestionsUseCase(getFetchLastActiveQuestionsEndpoint());
    }

    public QuestionsListController getQuestionsListController() {
        return new QuestionsListController(
                getFetchLastActiveQuestionsUseCase(),
                getScreensNavigator(),
                getToastsHelper()
        );
    }

    public ToastsHelper getToastsHelper() {
        return new ToastsHelper(getContext());
    }

    public ScreensNavigator getScreensNavigator() {
        return new ScreensNavigator(getFragmentFrameHelper());
    }

    private FragmentFrameHelper getFragmentFrameHelper() {
        return new FragmentFrameHelper(getActivity(), getFragmentFrameWrapper(), getFragmentManager());
    }

    private FragmentFrameWrapper getFragmentFrameWrapper() {
        return (FragmentFrameWrapper) getActivity();
    }

    public BackPressDispatcher getBackPressDispatcher() {
        return (BackPressDispatcher) getActivity();
    }

    public QuestionDetailsController getQuestionDetailsController() {
        return new QuestionDetailsController(getFetchQuestionDetailsUseCase(), getScreensNavigator(), getToastsHelper());
    }
}
