package dev.olaore.unittestingonandroid.tutorial_android_application.screens.questionslist;


import java.util.List;

import dev.olaore.unittestingonandroid.tutorial_android_application.questions.FetchLastActiveQuestionsUseCase;
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.Question;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.screensnavigator.ScreensNavigator;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.toastshelper.ToastsHelper;

public class QuestionsListController  implements
        QuestionsListViewMvc.Listener,
        FetchLastActiveQuestionsUseCase.Listener {

    private final FetchLastActiveQuestionsUseCase mFetchLastActiveQuestionsUseCase;
    private final ScreensNavigator mScreensNavigator;
    private final ToastsHelper mToastsHelper;

    private QuestionsListViewMvc mViewMvc;
    private List<Question> mQuestions;

    public QuestionsListController(FetchLastActiveQuestionsUseCase fetchLastActiveQuestionsUseCase,
                                   ScreensNavigator screensNavigator,
                                   ToastsHelper toastsHelper) {
        mFetchLastActiveQuestionsUseCase = fetchLastActiveQuestionsUseCase;
        mScreensNavigator = screensNavigator;
        mToastsHelper = toastsHelper;
    }

    public void bindView(QuestionsListViewMvc viewMvc) {
        mViewMvc = viewMvc;
    }

    public void onStart() {
        mViewMvc.registerListener(this);
        mFetchLastActiveQuestionsUseCase.registerListener(this);

        if (mQuestions != null) {
            mViewMvc.bindQuestions(mQuestions);
        } else {
            mViewMvc.showProgressIndication();
            mFetchLastActiveQuestionsUseCase.fetchLastActiveQuestionsAndNotify();
        }
    }

    public void onStop() {
        mViewMvc.unregisterListener(this);
        mFetchLastActiveQuestionsUseCase.unregisterListener(this);
    }

    @Override
    public void onQuestionClicked(Question question) {
        mScreensNavigator.toQuestionDetails(question.getId());
    }


    @Override
    public void onLastActiveQuestionsFetched(List<Question> questions) {
        mQuestions = questions;
        mViewMvc.hideProgressIndication();
        mViewMvc.bindQuestions(questions);
    }

    @Override
    public void onLastActiveQuestionsFetchFailed() {
        mViewMvc.hideProgressIndication();
        mToastsHelper.showUseCaseError();
    }
}
