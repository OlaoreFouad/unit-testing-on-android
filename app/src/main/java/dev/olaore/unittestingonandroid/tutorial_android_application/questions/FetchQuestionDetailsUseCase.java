package dev.olaore.unittestingonandroid.tutorial_android_application.questions;


import dev.olaore.unittestingonandroid.tutorial_android_application.common.BaseObservable;
import dev.olaore.unittestingonandroid.tutorial_android_application.common.time.TimeProvider;
import dev.olaore.unittestingonandroid.tutorial_android_application.networking.questions.FetchQuestionDetailsEndpoint;
import dev.olaore.unittestingonandroid.tutorial_android_application.networking.questions.QuestionSchema;

public class FetchQuestionDetailsUseCase extends BaseObservable<FetchQuestionDetailsUseCase.Listener> {



    public interface Listener {
        void onQuestionDetailsFetched(QuestionDetails questionDetails);
        void onQuestionDetailsFetchFailed();
    }

    private final FetchQuestionDetailsEndpoint mFetchQuestionDetailsEndpoint;
    private final TimeProvider mTimeProvider;

    public FetchQuestionDetailsUseCase(FetchQuestionDetailsEndpoint fetchQuestionDetailsEndpoint, TimeProvider timeProvider) {
        mFetchQuestionDetailsEndpoint = fetchQuestionDetailsEndpoint;
        mTimeProvider = timeProvider;
    }

    public void fetchQuestionDetailsAndNotify(String questionId) {
        mFetchQuestionDetailsEndpoint.fetchQuestionDetails(questionId, new FetchQuestionDetailsEndpoint.Listener() {
            @Override
            public void onQuestionDetailsFetched(QuestionSchema question) {
                notifySuccess(question);

            }

            @Override
            public void onQuestionDetailsFetchFailed() {
                notifyFailure();
            }
        });
    }

    private void notifyFailure() {
        for (Listener listener : getListeners()) {
            listener.onQuestionDetailsFetchFailed();
        }
    }

    private void notifySuccess(QuestionSchema questionSchema) {
        for (Listener listener : getListeners()) {
            listener.onQuestionDetailsFetched(
                    new QuestionDetails(
                            questionSchema.getId(),
                            questionSchema.getTitle(),
                            questionSchema.getBody()
                    ));
        }
    }
}
