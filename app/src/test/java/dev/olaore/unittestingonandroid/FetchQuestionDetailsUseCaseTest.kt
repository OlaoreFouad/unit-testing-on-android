package dev.olaore.unittestingonandroid

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dev.olaore.unittestingonandroid.tutorial_android_application.networking.questions.FetchQuestionDetailsEndpoint
import dev.olaore.unittestingonandroid.tutorial_android_application.networking.questions.QuestionSchema
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.FetchQuestionDetailsUseCase
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.QuestionDetails
import org.junit.Before
import org.junit.Test

const val QUESTION_ID = "questionId"
const val QUESTION_TITLE = "questionTitle"
const val QUESTION_BODY = "questionBody"
val questionDetails = QuestionDetails(QUESTION_ID, QUESTION_TITLE, QUESTION_BODY)

class FetchQuestionDetailsUseCaseTest {

    // region helper fields

    lateinit var systemUnderTest: FetchQuestionDetailsUseCase
    lateinit var endpointTd: EndpointTd
    lateinit var listener1: FetchQuestionDetailsUseCase.Listener
    lateinit var listener2: FetchQuestionDetailsUseCase.Listener

    // endregion helper fields

    // region test setup

    @Before
    fun setup() {
        endpointTd = EndpointTd()
        systemUnderTest = FetchQuestionDetailsUseCase(endpointTd, null)

        listener1 = mock()
        listener2 = mock()
    }

    // endregion test setup

    // region test methods

    // 1. when correct questionId is passed, listeners should be notified with accurate question
    @Test
    fun test_whenCorrectQuestionId_isPassed_shouldNotifyListenersWithQuestionObject() {
        // arrange
        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        // act
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)
        // assert
        verify(listener1).onQuestionDetailsFetched(
            argThat { id == questionDetails.id && title == questionDetails.title && body == questionDetails.body }
        )
        verify(listener2).onQuestionDetailsFetched(
            argThat { id == questionDetails.id && title == questionDetails.title && body == questionDetails.body }
        )
    }

    // 2. when wrong questionId is passed, listeners should be notified with failure event
    @Test
    fun test_whenWrongQuestionId_isPassed_shouldNotifyListenersWithFailureEvent() {
        // arrange
        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        // act
        systemUnderTest.fetchQuestionDetailsAndNotify("")
        // assert
        verify(listener1).onQuestionDetailsFetchFailed()
        verify(listener2).onQuestionDetailsFetchFailed()
    }

    // 3. when error occurs, listeners should be notified of a failure event
    @Test
    fun test_whenQuestionId_isPassed_withError_shouldNotifyListenersWithFailureEvent() {
        // arrange
        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        failure()
        // act
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)
        // assert
        verify(listener1).onQuestionDetailsFetchFailed()
        verify(listener2).onQuestionDetailsFetchFailed()
    }

    // endregion test methods

    // region helper methods

    private fun failure() {
        endpointTd.errorExists = true
    }

    // endregion helper methods

}

class EndpointTd : FetchQuestionDetailsEndpoint(null) {

    var errorExists = false

    override fun fetchQuestionDetails(questionId: String?, listener: Listener?) {

        if (errorExists) {
            listener?.onQuestionDetailsFetchFailed()
            return
        }

        if (questionId == QUESTION_ID) {
            listener?.onQuestionDetailsFetched(getQuestionSchemas())
        } else {
            listener?.onQuestionDetailsFetchFailed()
        }
    }

    private fun getQuestionSchemas(): QuestionSchema {
        return QuestionSchema(QUESTION_TITLE, QUESTION_ID, QUESTION_BODY)
    }

}