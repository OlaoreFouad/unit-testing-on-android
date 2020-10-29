package dev.olaore.unittestingonandroid.mocking

import com.nhaarman.mockitokotlin2.*
import dev.olaore.unittestingonandroid.test_doubles.example4.LoginUseCaseSync
import dev.olaore.unittestingonandroid.test_doubles.example4.authtoken.AuthTokenCache
import dev.olaore.unittestingonandroid.test_doubles.example4.eventbus.EventBusPoster
import dev.olaore.unittestingonandroid.test_doubles.example4.eventbus.LoggedInEvent
import dev.olaore.unittestingonandroid.test_doubles.example4.networking.LoginHttpEndpointSync
import dev.olaore.unittestingonandroid.test_doubles.example4.networking.NetworkErrorException
import org.junit.Assert.*;
import org.junit.Before
import org.junit.Test

const val USERNAME = "username"
const val PASSWORD = "password"
const val AUTH_TOKEN = "authToken"

class LoginUseCaseSyncTestMock {

    lateinit var loginHttpEndpointSync: LoginHttpEndpointSync
    lateinit var authTokenCache: AuthTokenCache
    lateinit var eventBusPoster: EventBusPoster

    lateinit var systemUnderTest: LoginUseCaseSync

    @Before
    fun setup() {
        loginHttpEndpointSync = mock()
        authTokenCache = mock()
        eventBusPoster = mock()

        systemUnderTest = LoginUseCaseSync(loginHttpEndpointSync, authTokenCache, eventBusPoster)
        success()
    }

    @Test
    fun test_usernameAndPassword_shouldBePassedToEndpoint() {
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(loginHttpEndpointSync).loginSync(argThat {
            this == USERNAME
        }, argThat { this == PASSWORD })
    }

    @Test
    fun test_whenLoginSucceeds_shouldCacheAuthToken() {
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(authTokenCache).cacheAuthToken(argThat { this == AUTH_TOKEN })
    }

    @Test
    fun test_whenLoginFails_dueToGeneralError_shouldNotCacheAuthToken() {
        generalError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(authTokenCache, never()).cacheAuthToken(any())
    }

    @Test
    fun test_whenLoginFails_dueToAuthError_shouldNotCacheAuthToken() {
        authError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(authTokenCache, never()).cacheAuthToken(any())
    }

    @Test
    fun test_whenLoginFails_dueToServerError_shouldNotCacheAuthToken() {
        serverError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(authTokenCache, never()).cacheAuthToken(any())
    }

    @Test
    fun test_whenLoginFails_dueToNetworkError_shouldNotCacheAuthToken() {
        networkError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(authTokenCache, never()).cacheAuthToken(any())
    }

    @Test
    fun test_whenLoginSucceeds_loggedInEvent_shouldBePosted() {
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(eventBusPoster).postEvent(argThat { this is LoggedInEvent })
    }

    @Test
    fun test_whenLoginFails_dueToGeneralError_shouldNotPostLoggedInEvent() {
        generalError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(eventBusPoster, never()).postEvent(any())
    }

    @Test
    fun test_whenLoginFails_dueToAuthError_shouldNotPostLoggedInEvent() {
        authError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(eventBusPoster, never()).postEvent(any())
    }

    @Test
    fun test_whenLoginFails_dueToServerError_shouldNotPostLoggedInEvent() {
        serverError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(eventBusPoster, never()).postEvent(any())
    }

    @Test
    fun test_whenLoginFails_dueToNetworkError_shouldNotPostLoggedInEvent() {
        networkError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(eventBusPoster, never()).postEvent(any())
    }

    @Test
    fun test_whenLoginSucceeds_shouldReturnSuccessResult() {
        val loginResult = systemUnderTest.loginSync(USERNAME, PASSWORD)
        assertEquals(loginResult, LoginUseCaseSync.UseCaseResult.SUCCESS)
    }

    @Test
    fun test_whenLoginFails_dueToGeneralError_shouldReturnFailureResult() {
        generalError()
        val loginResult = systemUnderTest.loginSync(USERNAME, PASSWORD)
        assertEquals(loginResult, LoginUseCaseSync.UseCaseResult.FAILURE)
    }

    @Test
    fun test_whenLoginFails_dueToAuthError_shouldReturnFailureResult() {
        authError()
        val loginResult = systemUnderTest.loginSync(USERNAME, PASSWORD)
        assertEquals(loginResult, LoginUseCaseSync.UseCaseResult.FAILURE)
    }

    @Test
    fun test_whenLoginFails_dueToServerError_shouldReturnFailureResult() {
        serverError()
        val loginResult = systemUnderTest.loginSync(USERNAME, PASSWORD)
        assertEquals(loginResult, LoginUseCaseSync.UseCaseResult.FAILURE)
    }

    @Test
    fun test_whenLoginFails_dueToNetworkError_shouldReturnFailureResult() {
        networkError()
        val loginResult = systemUnderTest.loginSync(USERNAME, PASSWORD)
        assertEquals(loginResult, LoginUseCaseSync.UseCaseResult.NETWORK_ERROR)
    }

    private fun success() {
        whenever(loginHttpEndpointSync.loginSync(USERNAME, PASSWORD))
            .thenReturn(LoginHttpEndpointSync.EndpointResult(
                LoginHttpEndpointSync.EndpointResultStatus.SUCCESS, AUTH_TOKEN
            ))
    }

    private fun generalError() {
        whenever(loginHttpEndpointSync.loginSync(USERNAME, PASSWORD))
            .thenReturn(LoginHttpEndpointSync.EndpointResult(
                LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, ""
            ))
    }

    private fun authError() {
        whenever(loginHttpEndpointSync.loginSync(USERNAME, PASSWORD))
            .thenReturn(LoginHttpEndpointSync.EndpointResult(
                LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, ""
            ))
    }

    private fun serverError() {
        whenever(loginHttpEndpointSync.loginSync(USERNAME, PASSWORD))
            .thenReturn(LoginHttpEndpointSync.EndpointResult(
                LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, ""
            ))
    }

    private fun networkError() {
        whenever(loginHttpEndpointSync.loginSync(USERNAME, PASSWORD))
            .doThrow(NetworkErrorException())
    }

}