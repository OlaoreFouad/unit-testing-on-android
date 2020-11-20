package dev.olaore.unittestingonandroid.tutorial_android_application

import com.nhaarman.mockitokotlin2.mock
import dev.olaore.unittestingonandroid.tutorial_android_application.questions.FetchQuestionDetailsUseCase
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.screensnavigator.ScreensNavigator
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.common.toastshelper.ToastsHelper
import dev.olaore.unittestingonandroid.tutorial_android_application.screens.questiondetails.QuestionDetailsController
import org.junit.Before
import org.junit.Test

// test cases
// 1. onStart of the fragment, initialize the controller, register required listeners and make the request to get question details
// 2. onStop, unregister listeners
// 3. onStart, make sure the questionId is bound to the controller
// 4. onStart, make sure the question is bound to the view

class QuestionDetailsControllerTest {

    lateinit var systemUnderTest: QuestionDetailsController

    // region helper fields

    lateinit var screensNavigator: ScreensNavigator
    lateinit var toastsHelper: ToastsHelper
    lateinit var useCaseTd: UseCaseTd

    // endregion helper fields

    @Before
    fun setup() {

        screensNavigator = mock()
        toastsHelper = mock()
        useCaseTd = UseCaseTd()

        systemUnderTest = QuestionDetailsController(useCaseTd, screensNavigator, toastsHelper)
    }

    // region test cases

    // 1. onStart of the fragment, initialize the controller, register required listeners and make the request to get question details
    @Test
    fun test_onStartOfController_success_shouldRegisterListeners() {

        systemUnderTest.onStart()

    }

    // endregion test cases

    // region helper methods

    // endregion helper methods

}

class UseCaseTd : FetchQuestionDetailsUseCase(null) {



}