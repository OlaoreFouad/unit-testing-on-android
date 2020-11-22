package dev.olaore.unittestingonandroid.util

import dev.olaore.unittestingonandroid.tutorial_android_application.questions.QuestionDetails

object Util {

    const val questionId = "QUESTION_ID"

    fun getQuestionDetails(id: String) = QuestionDetails(id, "Question Title", "Question Body")

}