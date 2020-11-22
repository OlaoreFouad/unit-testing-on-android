package dev.olaore.unittestingonandroid.tutorial_android_application.screens.questiondetails;


import dev.olaore.unittestingonandroid.tutorial_android_application.questions.FetchQuestionDetailsUseCase;
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.QuestionDetails;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.screensnavigator.ScreensNavigator;
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.toastshelper.ToastsHelper;

public class QuestionDetailsController implements FetchQuestionDetailsUseCase.Listener, QuestionDetailsViewMvc.Listener {

    private final FetchQuestionDetailsUseCase mFetchQuestionDetailsUseCase;
    private final ScreensNavigator mScreensNavigator;
    private final ToastsHelper mToastsHelper;

    private String mQuestionId;
    private QuestionDetailsViewMvc mViewMvc;

    public QuestionDetailsController(FetchQuestionDetailsUseCase fetchQuestionDetailsUseCase,
                                     ScreensNavigator screensNavigator,
                                     ToastsHelper toastsHelper) {
        mFetchQuestionDetailsUseCase = fetchQuestionDetailsUseCase;
        mScreensNavigator = screensNavigator;
        mToastsHelper = toastsHelper;
    }

    public void bindQuestionId(String questionId) {
        mQuestionId = questionId;
    }

    public void onStart() {
        mFetchQuestionDetailsUseCase.registerListener(this);
        mViewMvc.registerListener(this);

        mViewMvc.showProgressIndication();
        mFetchQuestionDetailsUseCase.fetchQuestionDetailsAndNotify(mQuestionId);
    }

    @Override
    public void onNavigateUpClicked() {
        mScreensNavigator.navigateUp();
    }

    @Override
    public void onQuestionDetailsFetched(QuestionDetails questionDetails) {
        mViewMvc.hideProgressIndication();
        mViewMvc.bindQuestion(questionDetails);
    }

    @Override
    public void onQuestionDetailsFetchFailed() {
        mViewMvc.hideProgressIndication();
        mToastsHelper.showUseCaseError();
    }

    public void bindView(QuestionDetailsViewMvc viewMvc) {
        mViewMvc = viewMvc;
    }

    public void onStop() {
        mViewMvc.unregisterListener(this);
        mFetchQuestionDetailsUseCase.unregisterListener(this);
    }
}
