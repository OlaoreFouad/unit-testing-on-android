package dev.olaore.unittestingonandroid.tutorial_android_application.screens.questiondetails;


import dev.olaore.unittestingonandroid.tutorial_android_application.questions.QuestionDetails;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.views.ObservableViewMvc;

public interface QuestionDetailsViewMvc extends ObservableViewMvc<QuestionDetailsViewMvc.Listener> {

    public interface Listener {
        void onNavigateUpClicked();
    }

    void bindQuestion(QuestionDetails question);

    void showProgressIndication();

    void hideProgressIndication();
}
