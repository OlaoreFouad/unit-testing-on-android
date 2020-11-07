package dev.olaore.unittestingonandroid.test_driven_development_test.exercise6

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.olaore.unittestingonandroid.test_driven_development.exercise7.FetchReputationUseCaseSync
import dev.olaore.unittestingonandroid.test_driven_development.exercise7.networking.GetReputationHttpEndpointSync
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

const val FETCHED_REPUTATION = 1
const val NO_REPUTATION = 0

class FetchReputationUseCaseSyncTest {

    // region helper fields
    lateinit var systemUnderTest: FetchReputationUseCaseSync
    lateinit var getReputationHttpEndpointSync: GetReputationHttpEndpointSync
    // endregion helper fields

    @Before
    fun setup() {
        getReputationHttpEndpointSync = mock()
        systemUnderTest = FetchReputationUseCaseSync(getReputationHttpEndpointSync)
        success()
    }

    // region test methods

//    1) If the server request completes successfully, then use case should indicate successful completion of the flow.
    @Test
    fun test_whenServerRequest_completesSuccessfully_shouldReturnSuccessResult() {
//    arrange
//    act
        val result = systemUnderTest.fetchReputationSync()
//    assert
        assertEquals(result.status,
            FetchReputationUseCaseSync.Companion.UseCaseResultStatus.SUCCESS
        )
    }

//    2) If the server request completes successfully, then the fetched reputation should be returned
    @Test
    fun test_whenServerRequest_completesSuccessfully_shouldReturnFetchedReputation() {
//        arrange
//        act
        val result = systemUnderTest.fetchReputationSync()
//        assert
        assertEquals(result.reputation, FETCHED_REPUTATION)
        verify(getReputationHttpEndpointSync).reputationSync
    }

//    3) If the server request fails for any reason (general error), the use case should indicate that the flow failed.
    @Test
    fun test_whenServerRequest_failsDueToGeneralError_shouldReturnFailureResponse() {
//        arrange
        generalError()
//        act
        val result = systemUnderTest.fetchReputationSync()
//        assert
        assertEquals(result.status, FetchReputationUseCaseSync.Companion.UseCaseResultStatus.FAILURE)
    }

//    4) If the server request fails for any reason (network error), the use case should indicate that the flow failed.
    @Test
    fun test_whenServerRequest_failsDueToNetworkError_shouldReturnFailureResponse() {
//        arrange
        networkError()
//        act
        val result = systemUnderTest.fetchReputationSync()
//        assert
        assertEquals(result.status, FetchReputationUseCaseSync.Companion.UseCaseResultStatus.FAILURE)
    }

    //    5) If the server request fails for any reason, the returned reputation should be 0.
    @Test
    fun test_whenServerRequest_failsDueToGeneralError_shouldReturnZeroAsReputation() {
//        arrange
        generalError()
//        act
        val result = systemUnderTest.fetchReputationSync()
//        assert
        assertEquals(result.reputation, NO_REPUTATION)
        verify(getReputationHttpEndpointSync).reputationSync
    }

    //    5) If the server request fails for any reason, the returned reputation should be 0.
    @Test
    fun test_whenServerRequest_failsDueToNetworkError_shouldReturnZeroAsReputation() {
//        arrange
        networkError()
//        act
        val result = systemUnderTest.fetchReputationSync()
//        assert
        assertEquals(result.reputation, NO_REPUTATION)
        verify(getReputationHttpEndpointSync).reputationSync
    }

    // endregion test methods

    // region helper methods

    private fun success() {
        whenever(getReputationHttpEndpointSync.reputationSync)
            .thenReturn(
                GetReputationHttpEndpointSync.EndpointResult(
                    GetReputationHttpEndpointSync.EndpointStatus.SUCCESS, FETCHED_REPUTATION
                )
            )
    }

    private fun generalError() {
        whenever(getReputationHttpEndpointSync.reputationSync)
            .thenReturn(
                GetReputationHttpEndpointSync.EndpointResult(
                    GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR, NO_REPUTATION
                )
            )
    }

    private fun networkError() {
        whenever(getReputationHttpEndpointSync.reputationSync)
            .thenReturn(
                GetReputationHttpEndpointSync.EndpointResult(
                    GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR, NO_REPUTATION
                )
            )
    }

    // endregion helper methods

}