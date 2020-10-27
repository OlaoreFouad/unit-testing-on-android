package dev.olaore.unittestingonandroid.test_doubles_test

import dev.olaore.unittestingonandroid.test_doubles.example4.networking.NetworkErrorException
import dev.olaore.unittestingonandroid.test_doubles.exercise4.FetchUserProfileUseCaseSync
import dev.olaore.unittestingonandroid.test_doubles.exercise4.networking.UserProfileHttpEndpointSync
import dev.olaore.unittestingonandroid.test_doubles.exercise4.users.User
import dev.olaore.unittestingonandroid.test_doubles.exercise4.users.UsersCache
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

const val testUserId = "jagbajantis"
const val testFullName = "Olaore Fouad"
const val testImageUri = "https://pexels.com/photo/2344jnj24jn8"

val testUser = User(testUserId, testFullName, testImageUri)

class FetchUserProfileUseCaseSyncTest {

    lateinit var systemUnderTest: FetchUserProfileUseCaseSync
    lateinit var userProfileHttpEndpointSyncTd: UserProfileHttpEndpointSyncTd
    lateinit var usersCacheTd: UsersCacheTd

    /*
    * 1. userId for user profile is being passed to the endpoint and saved there
    * 2. when general error occurs, error is returned
    * 3. when auth error occurs, error is returned
    * 4. when server error occurs, error is returned
    * 4b. when a network error occurs, an exception is thrown
    * 5. when user object is cached, it's actually saved and is equal to the recognised object stored in mem
    * 5b. when a query is made to get a user with mismatching user id, return null
    * 6. when a query is made to get the cached user with correct, the same object is returned as the one saved
    * */

    @Before
    fun setup() {
        userProfileHttpEndpointSyncTd = UserProfileHttpEndpointSyncTd()
        usersCacheTd = UsersCacheTd()
        systemUnderTest = FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd)
    }

    // 1. userId for user profile is being passed to the endpoint and saved there
    @Test
    fun test_userIdForGetUserProfile_isPassedToEndpoint() {
        systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertThat(
            userProfileHttpEndpointSyncTd.userId, CoreMatchers.`is`(testUserId)
        )
    }

    // 2. when general error occurs, error is returned
    @Test
    fun test_whenGeneralErrorOccurs_shouldReturnFailure() {
        userProfileHttpEndpointSyncTd.isGeneralError = true
        val fetchUserProfileResult = systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertThat(
            fetchUserProfileResult, CoreMatchers.`is`(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE)
        )
    }

    // 3. when auth error occurs, error is returned
    @Test
    fun test_whenAuthErrorOccurs_shouldReturnFailure() {
        userProfileHttpEndpointSyncTd.isAuthError = true
        val fetchUserProfileResult = systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertThat(
            fetchUserProfileResult, CoreMatchers.`is`(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE)
        )
    }

    // 4. when server error occurs, error is returned
    @Test
    fun test_whenServerErrorOccurs_shouldReturnFailure() {
        userProfileHttpEndpointSyncTd.isServerError = true
        val fetchUserProfileResult = systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertThat(
            fetchUserProfileResult, CoreMatchers.`is`(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE)
        )
    }

    // 4. when network error occurs, network error is returned
    @Test
    fun test_whenNetworkErrorOccurs_shouldReturnNetworkError() {
        userProfileHttpEndpointSyncTd.isNetworkError = true
        val fetchUserProfileResult = systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertThat(
            fetchUserProfileResult, CoreMatchers.`is`(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR)
        )
    }

    // 5. when user object is cached, it's actually saved and is equal to the recognised object stored in memory
    @Test
    fun test_whenUserObjectIsCached_shouldbeSavedAndStoredInMemory() {
        systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertThat(
            usersCacheTd.mUser?.userId, CoreMatchers.`is`(testUser.userId)
        )
        Assert.assertThat(
            usersCacheTd.mUser?.fullName, CoreMatchers.`is`(testUser.fullName)
        )
        Assert.assertThat(
            usersCacheTd.mUser?.imageUrl, CoreMatchers.`is`(testUser.imageUrl)
        )
    }

    // 5b. when get user profile fails with general error, user object should not be cached.
    @Test
    fun test_whenFetchUserProfileFails_withGeneralError_shouldNotCacheUsersObject() {
        userProfileHttpEndpointSyncTd.isGeneralError = true
        systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertEquals(
            usersCacheTd.mUser, null
        )
    }

    // 5b. when get user profile fails with auth error, user object should not be cached.
    @Test
    fun test_whenFetchUserProfileFails_withAuthError_shouldNotCacheUsersObject() {
        userProfileHttpEndpointSyncTd.isAuthError = true
        systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertEquals(
            usersCacheTd.mUser, null
        )
    }

    // 5b. when get user profile fails with server error, user object should not be cached.
    @Test
    fun test_whenFetchUserProfileFails_withServerError_shouldNotCacheUsersObject() {
        userProfileHttpEndpointSyncTd.isServerError = true
        systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertEquals(
            usersCacheTd.mUser, null
        )
    }

    // 5b. when get user profile fails with network error, user object should not be cached.
    @Test
    fun test_whenFetchUserProfileFails_withNetworkError_shouldNotCacheUsersObject() {
        userProfileHttpEndpointSyncTd.isNetworkError = true
        systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertEquals(
            usersCacheTd.mUser, null
        )
    }

    // get user profile with correct credentials return success result
    @Test
    fun test_fetchUserProfile_withCorrectCredentials_shouldReturnSuccessResult() {
        val loginResult = systemUnderTest.fetchUserProfileSync(testUserId)
        Assert.assertEquals(
            loginResult, FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS
        )
    }

    companion object {

        class UserProfileHttpEndpointSyncTd : UserProfileHttpEndpointSync {

            var userId: String? = ""

            var isGeneralError = false
            var isAuthError = false
            var isServerError = false
            var isNetworkError = false

            override fun getUserProfile(userId: String?): UserProfileHttpEndpointSync.EndpointResult {
                this.userId = userId

                return when {
                    isGeneralError -> UserProfileHttpEndpointSync.EndpointResult(
                        UserProfileHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, "", "", ""
                    )
                    isAuthError -> UserProfileHttpEndpointSync.EndpointResult(
                        UserProfileHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, "", "", ""
                    )
                    isServerError -> UserProfileHttpEndpointSync.EndpointResult(
                        UserProfileHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, "", "", ""
                    )
                    isNetworkError -> {
                        throw NetworkErrorException()
                    }
                    else -> UserProfileHttpEndpointSync.EndpointResult(
                        UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS, this.userId, testFullName, testImageUri
                    )
                }

            }
        }

        class UsersCacheTd : UsersCache {

            var mUser: User? = null
            var userId: String? = null

            override fun cacheUser(user: User?) {
                this.userId = user?.userId
                this.mUser = user
            }

            override fun getUser(userId: String?): User? {
                return if (userId == this.userId) {
                    mUser
                } else {
                    null
                }
            }
        }

    }

}