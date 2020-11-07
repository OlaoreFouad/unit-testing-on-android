package dev.olaore.unittestingonandroid.test_driven_development.exercise6;

import org.jetbrains.annotations.Nullable;

import dev.olaore.unittestingonandroid.test_driven_development.exercise6.users.User;

public interface FetchUserUseCaseSync {

    enum Status {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    class UseCaseResult {
        private final Status mStatus;

        @Nullable
        private final User mUser;

        public UseCaseResult(Status status, @Nullable User user) {
            mStatus = status;
            mUser = user;
        }

        public Status getStatus() {
            return mStatus;
        }

        @Nullable
        public User getUser() {
            return mUser;
        }
    }

    UseCaseResult fetchUserSync(String userId);

}
