package dev.olaore.unittestingonandroid.tutorial_android_application

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.FetchQuestionDetailsUseCase
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
    lateinit var toastsHelper: ToastsHelper
    lateinit var useCaseTd: UseCaseTd
    lateinit var questionDetailsViewMvc: QuestionDetailsViewMvc

    // endregion helper fields

    // 0. onBefore, bind view and question id to controller
    @Before
    fun setup() {

        screensNavigator = mock()
        toastsHelper = mock()
        questionDetailsViewMvc = mock()
        useCaseTd = UseCaseTd()

        systemUnderTest = QuestionDetailsController(useCaseTd, screensNavigator, toastsHelper)
        systemUnderTest.bindView(questionDetailsViewMvc)
        systemUnderTest.bindQuestionId(Util.questionId)
    }

    // region test cases

    // 1. onStart of the fragment, initialize the controller, register required listeners
    // 2. onStart of the fragment, make the request to get question details and showProgressIndication
    // 3. onStop of the fragment, unregister the listeners
    // 4. on successful question details fetched, hide progress indication and bind questions to view, also notify listeners
    // 5. on failure response, hide progress indication, show toast error, and notify listeners
    // 6. onNavigateUp clicked, screen navigator should navigate up

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

    // endregion test cases

    // region helper methods

    private fun failure() {
        useCaseTd.failureMode = true
    }

    // endregion helper methods

}

class UseCaseTd : FetchQuestionDetailsUseCase(null) {

    var failureMode = false

    override fun fetchQuestionDetailsAndNotify(questionId: String?) {

        listeners.forEach { listener ->
            if (!failureMode) {
                listener.onQuestionDetailsFetched(
                    Util.getQuestionDetails(Util.questionId)
                )
            } else {
                listener.onQuestionDetailsFetchFailed()
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