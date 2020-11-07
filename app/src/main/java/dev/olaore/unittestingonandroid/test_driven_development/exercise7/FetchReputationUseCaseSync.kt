package dev.olaore.unittestingonandroid.test_driven_development.exercise7

import dev.olaore.unittestingonandroid.test_driven_development.exercise7.networking.GetReputationHttpEndpointSync
import java.lang.RuntimeException

class FetchReputationUseCaseSync(
    private val httpEndpointSync: GetReputationHttpEndpointSync
) {

    companion object {

        enum class UseCaseResultStatus { SUCCESS, FAILURE }

        data class UseCaseResult(val status: UseCaseResultStatus, val reputation: Int)

    }

    fun fetchReputationSync(): UseCaseResult {
        val result = httpEndpointSync.reputationSync
        return when(result.status) {
            GetReputationHttpEndpointSync.EndpointStatus.SUCCESS -> UseCaseResult(UseCaseResultStatus.SUCCESS, result.reputation)
            GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR -> UseCaseResult(UseCaseResultStatus.FAILURE, 0)
            GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR -> UseCaseResult(UseCaseResultStatus.FAILURE, 0)
            else -> throw RuntimeException("Unknown Response Status")
        }
    }
}