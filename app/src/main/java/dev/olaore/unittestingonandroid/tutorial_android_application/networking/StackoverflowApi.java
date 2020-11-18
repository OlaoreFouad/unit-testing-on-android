package dev.olaore.unittestingonandroid.tutorial_android_application.networking;


import dev.olaore.unittestingonandroid.tutorial_android_application.common.Constants;
import dev.olaore.unittestingonandroid.tutorial_android_application.networking.questions.QuestionDetailsResponseSchema;
import dev.olaore.unittestingonandroid.tutorial_android_application.networking.questions.QuestionsListResponseSchema;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StackoverflowApi {

    @GET("/questions?key=" + Constants.STACKOVERFLOW_API_KEY + "&sort=activity&order=desc&site=stackoverflow&filter=withbody")
    Call<QuestionsListResponseSchema> fetchLastActiveQuestions(@Query("pagesize") Integer pageSize);

    @GET("/questions/{questionId}?key=" + Constants.STACKOVERFLOW_API_KEY + "&site=stackoverflow&filter=withbody")
    Call<QuestionDetailsResponseSchema> fetchQuestionDetails(@Path("questionId") String questionId);
}
