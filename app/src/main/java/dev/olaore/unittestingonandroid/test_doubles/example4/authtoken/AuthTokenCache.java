package dev.olaore.unittestingonandroid.test_doubles.example4.authtoken;

public interface AuthTokenCache {

    void cacheAuthToken(String authToken);

    String getAuthToken();
}
