package dev.olaore.unittestingonandroid.test_driven_development.solution_exercise_6

import dev.olaore.unittestingonandroid.test_driven_development.exercise6.FetchUserUseCaseSync
import dev.olaore.unittestingonandroid.test_driven_development.exercise6.networking.FetchUserHttpEndpointSync
import dev.olaore.unittestingonandroid.test_driven_development.exercise6.networking.NetworkErrorException
import dev.olaore.unittestingonandroid.test_driven_development.exercise6.users.User
import dev.olaore.unittestingonandroid.test_driven_development.exercise6.users.UsersCache
import java.lang.RuntimeException

class FetchUserUseCaseSyncImpl(
    private val httpEndpointSync: FetchUserHttpEndpointSync,
    private val usersCache: UsersCache
) : FetchUserUseCaseSync {

    override fun fetchUserSync(userId: String?): FetchUserUseCaseSync.UseCaseResult {
        val user = usersCache.getUser(userId)
        if (user == null) {
            try {
                val result = httpEndpointSync.fetchUserSync(userId)
                if (isSuccessfulFromEndpoint(result)) {
                    val resultingUser = User(result.userId, result.username)
                    usersCache.cacheUser(resultingUser)
                    return FetchUserUseCaseSync.UseCaseResult(
                        FetchUserUseCaseSync.Status.SUCCESS, resultingUser
                    )
                } else {
                    return FetchUserUseCaseSync.UseCaseResult(
                        FetchUserUseCaseSync.Status.FAILURE, null
                    )
                }
            } catch (ex: NetworkErrorException) {
                return FetchUserUseCaseSync.UseCaseResult(
                    FetchUserUseCaseSync.Status.NETWORK_ERROR, null
                )
            }
        } else {
            return FetchUserUseCaseSync.UseCaseResult(
                FetchUserUseCaseSync.Status.SUCCESS, user
            )
        }
    }

    private fun isSuccessfulFromEndpoint(result: FetchUserHttpEndpointSync.EndpointResult): Boolean {
        return result.status == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS
    }

}