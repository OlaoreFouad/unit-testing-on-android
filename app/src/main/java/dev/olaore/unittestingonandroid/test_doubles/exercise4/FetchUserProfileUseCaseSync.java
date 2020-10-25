package dev.olaore.unittestingonandroid.test_doubles.exercise4;


import dev.olaore.unittestingonandroid.test_doubles.example4.networking.NetworkErrorException;
import dev.olaore.unittestingonandroid.test_doubles.exercise4.networking.UserProfileHttpEndpointSync;
import dev.olaore.unittestingonandroid.test_doubles.exercise4.users.User;
import dev.olaore.unittestingonandroid.test_doubles.exercise4.users.UsersCache;

public class FetchUserProfileUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final UserProfileHttpEndpointSync mUserProfileHttpEndpointSync;
    private final UsersCache mUsersCache;

    public FetchUserProfileUseCaseSync(UserProfileHttpEndpointSync userProfileHttpEndpointSync,
                                       UsersCache usersCache) {
        mUserProfileHttpEndpointSync = userProfileHttpEndpointSync;
        mUsersCache = usersCache;
    }

    public UseCaseResult fetchUserProfileSync(String userId) {
        UserProfileHttpEndpointSync.EndpointResult endpointResult;
        try {
            // the bug here is that userId is not passed to endpoint
            endpointResult = mUserProfileHttpEndpointSync.getUserProfile("");
            // the bug here is that I don't check for successful result and it's also a duplication
            // of the call later in this method
            mUsersCache.cacheUser(
                    new User(userId, endpointResult.getFullName(), endpointResult.getImageUrl()));
        } catch (Exception e) {
            return UseCaseResult.NETWORK_ERROR;
        }

        if (isSuccessfulEndpointResult(endpointResult)) {
            mUsersCache.cacheUser(
                    new User(userId, endpointResult.getFullName(), endpointResult.getImageUrl()));
        }

        // the bug here is that I return wrong result in case of an unsuccessful server response
        return UseCaseResult.SUCCESS;
    }

    private boolean isSuccessfulEndpointResult(UserProfileHttpEndpointSync.EndpointResult endpointResult) {
        return endpointResult.getStatus() == UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS;
    }
}
