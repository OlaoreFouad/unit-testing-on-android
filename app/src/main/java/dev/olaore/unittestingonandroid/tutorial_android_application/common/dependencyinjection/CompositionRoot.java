package dev.olaore.unittestingonandroid.tutorial_android_application.common.dependencyinjection;


import dev.olaore.unittestingonandroid.tutorial_android_application.common.Constants;
import dev.olaore.unittestingonandroid.tutorial_android_application.networking.StackoverflowApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompositionRoot {

    private Retrofit mRetrofit;

    private Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public StackoverflowApi getStackoverflowApi() {
        return getRetrofit().create(StackoverflowApi.class);
    }
}
