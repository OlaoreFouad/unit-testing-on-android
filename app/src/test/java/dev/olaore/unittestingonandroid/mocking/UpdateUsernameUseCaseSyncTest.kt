package dev.olaore.unittestingonandroid.mocking

import com.nhaarman.mockitokotlin2.*
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.UpdateUsernameUseCaseSync
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.eventbus.EventBusPoster
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.eventbus.UserDetailsChangedEvent
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.networking.NetworkErrorException
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.users.User
import dev.olaore.unittestingonandroid.mockito_fundamentals.exercise5.users.UsersCache
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

const val _USERNAME = "username"
const val _USERID = "userId"

class UpdateUsernameUseCaseSyncTest {

    private lateinit var updateUsernameHttpEndpointSync: UpdateUsernameHttpEndpointSync
    private lateinit var eventBusPoster: EventBusPoster
    private lateinit var usersCache: UsersCache

    private lateinit var systemUnderTest: UpdateUsernameUseCaseSync

    @Before
    fun setup() {
        updateUsernameHttpEndpointSync = mock()
        eventBusPoster = mock()
        usersCache = mock()

        systemUnderTest = UpdateUsernameUseCaseSync(updateUsernameHttpEndpointSync, usersCache, eventBusPoster)
        success()
    }

    /*
    * 1. test if username to be updated is getting to the endpoint
    * 2. test if username is updated successfully, new username should be cached
    * 3. test if username update fails due to general error, new username should not be cached
    * 4. test if username update fails due to auth error, new username should not be cached
    * 5. test if username update fails due to server error, new username should not be cached
    * 6. test if username update fails due to network error, new username should not be cached
    * 7. test if username update is successful, username changed event is posted
    * 8. test if username update fails due to general error, usernameChangedEvent is not posted
    * 9. test if username update fails due to auth error, usernameChangedEvent is not posted
    * 10. test if username update fails due to server error, usernameChangedEvent is not posted
    * 11. test if username update fails due to network error, usernameChangedEvent is not posted
    * 12. test if username update is successful, success result is returned
    * 13. test if username update fails due to general error, failure result is returned
    * 14. test if username update fails due to auth error, failure result is returned
    * 15. test if username update fails due to server error, failure result is returned
    * 16. test if username update fails due to network error, failure result is returned
    * */

    // 1. test if username to be updated is getting to the endpoint
    @Test
    fun test_ifUsername_toBeUpdated_getsToTheEndpoint_withUserID() {
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(updateUsernameHttpEndpointSync).updateUsername(
            argThat { this == _USERID }, argThat { this == _USERNAME }
        )
    }

    // 2. test if username is updated successfully, new username should be cached
    @Test
    fun test_whenUsername_updatesSuccessful_shouldCacheNewUserObject() {
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(usersCache).cacheUser(
            argThat { this.userId == _USERID && this.username == _USERNAME }
        )
    }

    // 3. test if username update fails due to general error, new username should not be cached
    @Test
    fun test_whenUsername_updateFails_dueToGeneralError_shouldNotCacheUserObject() {
        generalError()
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(usersCache, never()).cacheUser(any())
    }

    // 4. test if username update fails due to auth error, new username should not be cached
    @Test
    fun test_whenUsername_updateFails_dueToAuthError_shouldNotCacheUserObject() {
        authError()
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(usersCache, never()).cacheUser(any())
    }

    // 5. test if username update fails due to server error, new username should not be cached
    @Test
    fun test_whenUsername_updateFails_dueToServerError_shouldNotCacheUserObject() {
        serverError()
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(usersCache, never()).cacheUser(any())
    }

    // 6. test if username update fails due to network error, new username should not be cached
    @Test
    fun test_whenUsername_updateFails_dueToNetworkError_shouldNotCacheUserObject() {
        networkError()
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(usersCache, never()).cacheUser(any())
    }

    // 7. test if username update is successful, username changed event is posted
    @Test
    fun test_whenUsername_updatesSuccessfully_shouldPostUsernameChangedEvent() {
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(eventBusPoster).postEvent(argThat { this is UserDetailsChangedEvent })
    }

    // 8. test if username update fails due to general error, usernameChangedEvent is not posted
    @Test
    fun test_whenUsernameUpdateFails_dueToGeneralError_shouldNotPostUserDetailsChangedEvent() {
        generalError()
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(eventBusPoster, never()).postEvent(any())
    }

    // 9. test if username update fails due to auth error, usernameChangedEvent is not posted
    @Test
    fun test_whenUsernameUpdateFails_dueToAuthError_shouldNotPostUserDetailsChangedEvent() {
        authError()
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(eventBusPoster, never()).postEvent(any())
    }

    // 10. test if username update fails due to server error, usernameChangedEvent is not posted
    @Test
    fun test_whenUsernameUpdateFails_dueToServerError_shouldNotPostUserDetailsChangedEvent() {
        serverError()
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(eventBusPoster, never()).postEvent(any())
    }

    // 11. test if username update fails due to network error, usernameChangedEvent is not posted
    @Test
    fun test_whenUsernameUpdateFails_dueToNetworkError_shouldNotPostUserDetailsChangedEvent() {
        networkError()
        systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        verify(eventBusPoster, never()).postEvent(any())
    }

    // 12. test if username update is successful, success result is returned
    @Test
    fun test_whenUsernameUpdate_isSuccessful_shouldReturnSuccessResult() {
        val updateResult = systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        assertEquals(updateResult, UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS)
    }

    // 13. test if username fails due to general error, failure result is returned
    @Test
    fun test_whenUsernameUpdate_fails_dueToGeneralError_shouldReturnFailureResult() {
        generalError()
        val updateResult = systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        assertEquals(updateResult, UpdateUsernameUseCaseSync.UseCaseResult.FAILURE)
    }

    // 14. test if username fails due to auth error, failure result is returned
    @Test
    fun test_whenUsernameUpdate_fails_dueToAuthError_shouldReturnFailureResult() {
        authError()
        val updateResult = systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        assertEquals(updateResult, UpdateUsernameUseCaseSync.UseCaseResult.FAILURE)
    }

    // 15. test if username fails due to server error, failure result is returned
    @Test
    fun test_whenUsernameUpdate_fails_dueToServerError_shouldReturnFailureResult() {
        serverError()
        val updateResult = systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        assertEquals(updateResult, UpdateUsernameUseCaseSync.UseCaseResult.FAILURE)
    }

    // 16. test if username fails due to network error, failure result is returned
    @Test
    fun test_whenUsernameUpdate_fails_dueToNetworkError_shouldReturnNetworkErrorResult() {
        networkError()
        val updateResult = systemUnderTest.updateUsernameSync(_USERID, _USERNAME)
        assertEquals(updateResult, UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR)
    }

    private fun success() {
        whenever(updateUsernameHttpEndpointSync.updateUsername(_USERID, _USERNAME))
            .thenReturn(
                UpdateUsernameHttpEndpointSync.EndpointResult(
                    UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS, _USERID, _USERNAME
                ))
    }

    private fun generalError() {
        whenever(updateUsernameHttpEndpointSync.updateUsername(_USERID, _USERNAME))
            .thenReturn(
                UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, "", "")
            )
    }

    private fun authError() {
        whenever(updateUsernameHttpEndpointSync.updateUsername(_USERID, _USERNAME))
            .thenReturn(
                UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, "", "")
            )
    }

    private fun serverError() {
        whenever(updateUsernameHttpEndpointSync.updateUsername(_USERID, _USERNAME))
            .thenReturn(
                UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, "", "")
            )
    }

    private fun networkError() {
        whenever(updateUsernameHttpEndpointSync.updateUsername(_USERID, _USERNAME))
            .doThrow(NetworkErrorException())
    }

}