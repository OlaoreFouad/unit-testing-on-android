package dev.olaore.unittestingonandroid.tutorial_android_application.screens.questiondetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dev.olaore.unittestingonandroid.tutorial_android_application.questions.FetchQuestionDetailsUseCase;
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.QuestionDetails;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.controllers.BaseFragment;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.screensnavigator.ScreensNavigator;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.toastshelper.ToastsHelper;


public class QuestionDetailsFragment extends BaseFragment {

    private static final String ARG_QUESTION_ID = "ARG_QUESTION_ID";

    public static QuestionDetailsFragment newInstance(String questionId) {
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION_ID, questionId);
        QuestionDetailsFragment fragment = new QuestionDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private QuestionDetailsViewMvc mViewMvc;

    private QuestionDetailsController mQuestionDetailsController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestionDetailsController = getCompositionRoot().getQuestionDetailsController();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mQuestionDetailsController.bindView(mViewMvc);
        mQuestionDetailsController.bindQuestionId(getQuestionId());

        return mViewMvc.getRootView();
    }

    @Override
    public void onStart() {
        super.onStart();
        mQuestionDetailsController.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mQuestionDetailsController.onStop();
    }

    private String getQuestionId() {
        return getArguments().getString(ARG_QUESTION_ID);
    }

}
