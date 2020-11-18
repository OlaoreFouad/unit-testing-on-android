package dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.screensnavigator;


import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.fragmentframehelper.FragmentFrameHelper;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questiondetails.QuestionDetailsFragment;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist.QuestionsListFragment;

public class ScreensNavigator {

    private FragmentFrameHelper mFragmentFrameHelper;

    public ScreensNavigator(FragmentFrameHelper fragmentFrameHelper) {
        mFragmentFrameHelper = fragmentFrameHelper;
    }

    public void toQuestionDetails(String questionId) {
        mFragmentFrameHelper.replaceFragment(QuestionDetailsFragment.newInstance(questionId));
    }

    public void toQuestionsList() {
        mFragmentFrameHelper.replaceFragmentAndClearBackstack(QuestionsListFragment.newInstance());
    }

    public void navigateUp() {
        mFragmentFrameHelper.navigateUp();
    }
}
