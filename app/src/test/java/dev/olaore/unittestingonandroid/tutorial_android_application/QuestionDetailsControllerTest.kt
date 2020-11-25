package dev.olaore.unittestingonandroid.tutorial_android_application

import com.nhaarman.mockitokotlin2.*
import dev.olaore.unittestingonandroid.QUESTION_ID
import dev.olaore.unittestingonandroid.tutorial_android_application.common.time.TimeProvider
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.FetchQuestionDetailsUseCase
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.QuestionDetails
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.screensnavigator.ScreensNavigator
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.toastshelper.ToastsHelper
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questiondetails.QuestionDetailsController
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questiondetails.QuestionDetailsViewMvc
import dev.olaore.unittestingonandroid.util.Util
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException


class QuestionDetailsControllerTest {

    lateinit var systemUnderTest: QuestionDetailsController

    // region helper fields

    lateinit var screensNavigator: ScreensNavigator
    lateinit var timeProvider: TimeProvider
    lateinit var toastsHelper: ToastsHelper
    lateinit var useCaseTd: UseCaseTd
    lateinit var questionDetailsViewMvc: QuestionDetailsViewMvc

    // endregion helper fields

    // 0. onBefore, bind view and question id to controller
    @Before
    fun setup() {

        screensNavigator = mock()
        toastsHelper = mock()
        timeProvider = mock()
        questionDetailsViewMvc = mock()
        useCaseTd = UseCaseTd(timeProvider)

        systemUnderTest = QuestionDetailsController(useCaseTd, screensNavigator, toastsHelper)
        systemUnderTest.bindView(questionDetailsViewMvc)
        systemUnderTest.bindQuestionId(Util.questionId)
    }

    // region test cases

    // 1. onStart of the fragment, initialize the controller, register required listeners
    // 2. onStart of the fragment, make the request to get question details and showProgressIndication
    @Test
    fun test_onStartOfController_success_shouldRegisterListeners() {
//        arrange
        val questionDetails = Util.getQuestionDetails(Util.questionId)

//        act
        systemUnderTest.onStart()

//        assert
        verify(questionDetailsViewMvc).registerListener(systemUnderTest)
        verify(questionDetailsViewMvc).showProgressIndication()
        verify(questionDetailsViewMvc).bindQuestion(
            argThat { id == questionDetails.id && title == questionDetails.title && body == questionDetails.body }
        )
        useCaseTd.verifyListenerWasRegistered(systemUnderTest)
    }

    // 3. onStop of the fragment, unregister the listeners
    @Test
    fun test_whenFragmentIsStopped_listeners_shouldBeUnregistered() {
//        arrange
//        act
        systemUnderTest.onStop()
//        assert
        verify(questionDetailsViewMvc).unregisterListener(systemUnderTest)
        useCaseTd.verifyListenerWasUnregistered(systemUnderTest)
    }

    // 4. on successful question details fetched, hide progress indication and bind questions to view, also notify listeners
    @Test
    fun test_whenOperationToGetDetails_succeeds_shouldHideProgressIndication_andBindQuestionsToView() {
//        arrange
        val questionDetails = Util.getQuestionDetails(Util.questionId)
//        act
        systemUnderTest.onStart()
//        assert
        verify(questionDetailsViewMvc).bindQuestion(
            argThat { id == questionDetails.id && title == questionDetails.title && body == questionDetails.body }
        )
        verify(questionDetailsViewMvc).hideProgressIndication()
    }

    // 5. on failure response, hide progress indication, show toast error, and notify listeners
    @Test
    fun test_whenOperationToGetDetails_fails_shouldHideProgressIndication_andShowToastError() {
//        arrange
        failure()
//        act
        systemUnderTest.onStart()
//        assert
        verify(questionDetailsViewMvc).hideProgressIndication()
        verify(toastsHelper).showUseCaseError()
    }

    // 6. onNavigateUp clicked, screen navigator should navigate up
    @Test
    fun test_whenNavigateUp_clicked_shouldInvokeScreensNavigateObject() {
//        arrange
//        act
        systemUnderTest.onNavigateUpClicked()
//        assert
        verify(screensNavigator).navigateUp()
    }

    // 6b. test when call is made to usecase for first time, data is cached



    // 7. test when call is made to the usecase for data and cache timeout is still in session (less than), return cached data
    @Test
    fun test_whenCallIsMade_forData_andTimeoutHasntElapsed_shouldReturnCachedData() {
//        arrange
        val questionDetails = Util.getQuestionDetails(QUESTION_ID)
        whenever(timeProvider.currentTimestamp)
            .thenReturn(0L)
//        act
        systemUnderTest.onStart()
        systemUnderTest.onStop()
        whenever(timeProvider.currentTimestamp).thenReturn(60000L)
        systemUnderTest.onStart()

//        assert
        verify(questionDetailsViewMvc, times(2)).bindQuestion(
            argThat { id == questionDetails.id && title == questionDetails.title && body == questionDetails.body }
        )
    }



    // 8. test when call is made to the usecase for data and cache timeout is elapsed, make call to network


    // endregion test cases

    // region helper methods

    private fun failure() {
        useCaseTd.failureMode = true
    }

    // endregion helper methods

}

class UseCaseTd(private val timeProvider: TimeProvider) : FetchQuestionDetailsUseCase(null, timeProvider) {

    var failureMode = false
    private val CACHE_TIMEOUT_MS = 60000
    var lastQuestionId = ""
    var lastCachedTime = 0L
    var mQuestionDetails: QuestionDetails? = null

    override fun fetchQuestionDetailsAndNotify(questionId: String?) {

        if (questionId == lastQuestionId && timeProvider.currentTimestamp < (lastCachedTime + CACHE_TIMEOUT_MS)) {
            listeners.forEach { listener ->
                if (!failureMode) {
                    listener.onQuestionDetailsFetched(mQuestionDetails)
                } else {
                    listener.onQuestionDetailsFetchFailed()
                }
            }
        } else {
            mQuestionDetails = Util.getQuestionDetails(questionId!!)
            lastQuestionId = questionId
            listeners.forEach { listener ->
                if (!failureMode) {
                    listener.onQuestionDetailsFetched(mQuestionDetails)
                } else {
                    listener.onQuestionDetailsFetchFailed()
                }
            }
        }
    }

    fun verifyListenerWasRegistered(controller: QuestionDetailsController) {
        for (listener in listeners) {
            if (listener == controller) {
                return
            }
        }
        throw RuntimeException("Listener has not been registered")
    }

    fun verifyListenerWasUnregistered(controller: QuestionDetailsController) {
        for (listener in listeners) {
            if (listener == controller) {
                throw RuntimeException("Listener has not been unregistered")
            }
        }
        return
    }

}