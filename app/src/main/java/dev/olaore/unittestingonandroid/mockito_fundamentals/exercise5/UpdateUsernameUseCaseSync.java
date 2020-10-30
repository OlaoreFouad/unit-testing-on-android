package dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5;


import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.eventbus.EventBusPoster;
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.networking.NetworkErrorException;
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.users.User;
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.users.UsersCache;

public class UpdateUsernameUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final UpdateUsernameHttpEndpointSync mUpdateUsernameHttpEndpointSync;
    private final UsersCache mUsersCache;
    private final EventBusPoster mEventBusPoster;

    public UpdateUsernameUseCaseSync(UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSync,
                                     UsersCache usersCache,
                                     EventBusPoster eventBusPoster) {
        mUpdateUsernameHttpEndpointSync = updateUsernameHttpEndpointSync;
        mUsersCache = usersCache;
        mEventBusPoster = eventBusPoster;
    }

    public UseCaseResult updateUsernameSync(String userId, String username) {
        UpdateUsernameHttpEndpointSync.EndpointResult endpointResult = null;
        try {
            endpointResult = mUpdateUsernameHttpEndpointSync.updateUsername(userId, username);
        } catch (NetworkErrorException e) {
            return UseCaseResult.NETWORK_ERROR;
        }

        if (isSuccessfulEndpointResult(endpointResult)) {
            User user = new User(endpointResult.getUserId(), endpointResult.getUsername());
            mEventBusPoster.postEvent(new UserDetailsChangedEvent(new User(userId, username)));
            mUsersCache.cacheUser(user);
            return UseCaseResult.SUCCESS;
        } else {
            return UseCaseResult.FAILURE;
        }
    }

    private boolean isSuccessfulEndpointResult(UpdateUsernameHttpEndpointSync.EndpointResult endpointResult) {
        return endpointResult.getStatus() == UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS;
    }
}
