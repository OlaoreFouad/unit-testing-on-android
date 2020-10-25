package dev.olaore.unittestingonandroid.test_doubles_test

import dev.olaore.unittestingonandroid.test_doubles.example4.LoginUseCaseSync
import dev.olaore.unittestingonandroid.test_doubles.example4.authtoken.AuthTokenCache
import dev.olaore.unittestingonandroid.test_doubles.example4.eventbus.EventBusPoster
import dev.olaore.unittestingonandroid.test_doubles.example4.eventbus.LoggedInEvent
import dev.olaore.unittestingonandroid.test_doubles.example4.networking.LoginHttpEndpointSync
import dev.olaore.unittestingonandroid.test_doubles.example4.networking.NetworkErrorException
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.internal.runners.JUnit38ClassRunner
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Test Cases worth testing
 * 1. Login parameters username and password are passed into the endpoint
 * 2. if login succeeds, save auth token in memory
 * 3. if login fails, existing auth token should not be overriden (although I don't think that should be the case)
 * 4. when a request for the auth token is made, the last saved auth token should be returned, null if no successful login session has taken place (stop here)
 * 5. when login succeeds, event bus poster class should post a LoggedInEvent event
 * 5b. when login fails,
 * 6. when login succeeds, a SUCCESS result should be returned
 * 7. when login fails, a FAILURE result should be returned
 * 8. when login fails due to a network error, a NETWORK_ERROR should be returned.
 * */

const val USERNAME = "username"
const val PASSWORD = "password"
const val AUTH_TOKEN = "authToken"


class LoginUseCaseSyncTest {

    lateinit var systemUnderTest: LoginUseCaseSync

    lateinit var authTokenCacheTd: AuthTokenCacheTd
    lateinit var loginHttpEndpointSyncTd: LoginHttpEndpointSyncTd
    lateinit var eventBusPosterTd: EventBusPosterTd

    @Before
    fun setup() {
        authTokenCacheTd = AuthTokenCacheTd()
        loginHttpEndpointSyncTd = LoginHttpEndpointSyncTd()
        eventBusPosterTd = EventBusPosterTd()
        systemUnderTest = LoginUseCaseSync(loginHttpEndpointSyncTd, authTokenCacheTd, eventBusPosterTd)
    }

    @Test
    fun test_loginParametersArePassedToTheEndpoint() {
        val username = USERNAME
        val password = PASSWORD

        systemUnderTest.loginSync(username, password)

        Assert.assertThat(loginHttpEndpointSyncTd.mUsername, CoreMatchers.`is`(username))
        Assert.assertThat(loginHttpEndpointSyncTd.mPassword, CoreMatchers.`is`(password))
    }

    @Test
    fun test_whenLoginSucceeds_autTokenIsSavedInMemory() {
        val authToken = AUTH_TOKEN

        systemUnderTest.loginSync(USERNAME, PASSWORD)

        Assert.assertThat(authTokenCacheTd.authToken, CoreMatchers.`is`(authToken))

    }

    @Test
    fun test_whenLoginFails_withGeneralError_emptyAuthTokenIsSaved() {
        val authToken = ""

        loginHttpEndpointSyncTd.isGeneralError = true
        systemUnderTest.loginSync(USERNAME, PASSWORD)

        Assert.assertThat(authTokenCacheTd.authToken, CoreMatchers.`is`(authToken))

    }

    @Test
    fun test_whenLoginFails_withAuthError_emptyAuthTokenIsSaved() {
        val authToken = ""

        loginHttpEndpointSyncTd.isAuthError = true
        systemUnderTest.loginSync(USERNAME, PASSWORD)

        Assert.assertThat(authTokenCacheTd.authToken, CoreMatchers.`is`(authToken))

    }

    @Test
    fun test_whenLoginFails_withServerError_emptyAuthTokenIsSaved() {
        val authToken = ""

        loginHttpEndpointSyncTd.isServerError = true
        systemUnderTest.loginSync(USERNAME, PASSWORD)

        Assert.assertThat(authTokenCacheTd.authToken, CoreMatchers.`is`(authToken))

    }

    @Test
    fun test_whenLoginSucceeds_loggedInEventIsPosted() {
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        Assert.assertThat(eventBusPosterTd.mEvent, CoreMatchers.`is`(CoreMatchers.instanceOf(LoggedInEvent::class.java)))
    }

    @Test
    fun test_whenLoginFails_dueToGeneralError_noInteractionOnEventBusPosterIsRecorded() {
        loginHttpEndpointSyncTd.isGeneralError = true
        loginHttpEndpointSyncTd.loginSync(USERNAME, PASSWORD)
        Assert.assertThat(eventBusPosterTd.mInteractions, CoreMatchers.`is`(0))
    }

    @Test
    fun test_whenLoginFails_dueToAuthError_noInteractionOnEventBusPosterIsRecorded() {
        loginHttpEndpointSyncTd.isAuthError = true
        loginHttpEndpointSyncTd.loginSync(USERNAME, PASSWORD)
        Assert.assertThat(eventBusPosterTd.mInteractions, CoreMatchers.`is`(0))
    }

    @Test
    fun test_whenLoginFails_dueToServerError_noInteractionOnEventBusPosterIsRecorded() {
        loginHttpEndpointSyncTd.isServerError = true
        loginHttpEndpointSyncTd.loginSync(USERNAME, PASSWORD)
        Assert.assertThat(eventBusPosterTd.mInteractions, CoreMatchers.`is`(0))
    }

    @Test
    fun test_correctLoginCredentials_shouldReturnSuccessResult() {
        val loginResult = systemUnderTest.loginSync(USERNAME, PASSWORD)
        Assert.assertThat(loginResult, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.SUCCESS))
    }

    @Test
    fun test_loginCredentials_withGeneralErrorRoadblock_shouldReturnFailureResult() {
        loginHttpEndpointSyncTd.isGeneralError = true
        val loginResult = systemUnderTest.loginSync(USERNAME, PASSWORD)
        Assert.assertThat(loginResult, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    fun test_loginCredentials_withAuthErrorRoadblock_shouldReturnFailureResult() {
        loginHttpEndpointSyncTd.isAuthError = true
        val loginResult = systemUnderTest.loginSync(USERNAME, PASSWORD)
        Assert.assertThat(loginResult, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    //    9. login with username and password with server error roadblock should return failure result
    @Test
    fun test_loginCredentials_withServerErrorRoadblock_shouldReturnFailureResult() {
        loginHttpEndpointSyncTd.isServerError = true
        val loginResult = systemUnderTest.loginSync(USERNAME, PASSWORD)
        Assert.assertThat(loginResult, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    fun test_loginCredentials_withNetworkErrorRoadblock_shouldReturnFailureResult() {
        loginHttpEndpointSyncTd.isNetworkError = true
        val loginResult = systemUnderTest.loginSync(USERNAME, PASSWORD)
        Assert.assertThat(loginResult, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR))
    }


    companion object {

        class LoginHttpEndpointSyncTd : LoginHttpEndpointSync {

            var mUsername: String? = ""
            var mPassword: String? = ""

            var isGeneralError = false
            var isAuthError = false
            var isServerError = false
            var isNetworkError = false

            override fun loginSync(
                username: String?,
                password: String?
            ): LoginHttpEndpointSync.EndpointResult {

                when {
                    isGeneralError -> {
                        return LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, "")
                    }
                    isAuthError -> {
                        return LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, "")
                    }
                    isServerError -> {
                        return LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, "")
                    }
                    isNetworkError -> {
                        throw NetworkErrorException()
                    }
                    else -> {
                        mUsername = username
                        mPassword = password

                        return LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SUCCESS, AUTH_TOKEN)
                    }
                }
            }

        }

        class AuthTokenCacheTd : AuthTokenCache {

            var mAuthToken: String? = ""

            override fun cacheAuthToken(authToken: String?) {
                mAuthToken = authToken
            }

            override fun getAuthToken(): String {
                return mAuthToken!!
            }
        }

        class EventBusPosterTd : EventBusPoster {

            lateinit var mEvent: Any
            var mInteractions = 0

            override fun postEvent(event: Any?) {
                mEvent = event!!
                mInteractions++
            }
        }

    }

}
