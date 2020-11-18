package dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist;

import java.util.List;

import dev.olaore.unittestingonandroid.tutorial_android_application.questions.Question;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.views.ObservableViewMvc;

public interface QuestionsListViewMvc extends ObservableViewMvc<QuestionsListViewMvc.Listener> {

    public interface Listener {
        void onQuestionClicked(Question question);
    }

    void bindQuestions(List<Question> questions);

    void showProgressIndication();

    void hideProgressIndication();

}
