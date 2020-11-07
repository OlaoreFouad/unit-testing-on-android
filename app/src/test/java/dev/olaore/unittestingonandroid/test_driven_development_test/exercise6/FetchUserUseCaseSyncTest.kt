package dev.olaore.unittestingonandroid.test_driven_development_test.exercise6

import com.nhaarman.mockitokotlin2.*
import dev.olaore.unittestingonandroid.test_driven_development.exercise6.networking.FetchUserHttpEndpointSync
import dev.olaore.unittestingonandroid.test_driven_development.exercise6.networking.NetworkErrorException
import dev.olaore.unittestingonandroid.test_driven_development.exercise6.users.User
import dev.olaore.unittestingonandroid.test_driven_development.exercise6.users.UsersCache
import dev.olaore.unittestingonandroid.test_driven_development.solution_exercise_6.FetchUserUseCaseSyncImpl
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*;

const val USER_ID = "userId"
const val WRONG_USER_ID = "wrongUserId"
const val USER_NAME = "OlaOlaore"

class FetchUserUseCaseSyncTest {

    lateinit var fetchUserHttpEndpointSync: FetchUserHttpEndpointSync
    lateinit var usersCache: UsersCache

    lateinit var systemUnderTest: FetchUserUseCaseSyncImpl

    @Before
    fun setup() {
        fetchUserHttpEndpointSync = mock()
        usersCache = mock()

        systemUnderTest = FetchUserUseCaseSyncImpl(fetchUserHttpEndpointSync, usersCache)
    }

    // 1. when user id is supplied, and it's not found in cache, make call to the server
    // 2. when user id is supplied and there is an issue due to a general error, return failure response
    // 3. when user obj from server is returned, store it in the cache
    // 4. if user id is in cache, then return cached object

    @Test
    fun test_whenUserIdSupplied_isNotFoundInCache_shouldReturnUserFromServer() {
        // act
        whenever(fetchUserHttpEndpointSync.fetchUserSync(USER_ID))
            .thenReturn(FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.SUCCESS, USER_ID, "OlaOlaore"
            ))

        // assert
        verify(usersCache, never()).getUser(any())
    }

    @Test
    fun test_whenUserIdIsSupplied_andGeneralErrorOccurs_shouldNotCacheUserObject() {
//        arrange
        generalError()

//        act
        systemUnderTest.fetchUserSync(USER_ID)

//        assert
        verify(usersCache, never()).cacheUser(any())
    }

    @Test
    fun test_whenUserIdIsSupplied_andAuthErrorOccurs_shouldNotCacheUserObject() {
//        arrange
        authError()

//        act
        systemUnderTest.fetchUserSync(USER_ID)

//        assert
        verify(usersCache, never()).cacheUser(any())
    }

    @Test
    fun test_whenUserIdIsSupplied_andNetworkErrorOccurs_shouldNotCacheUserObject() {
//        arrange
        networkError()

//        act
        systemUnderTest.fetchUserSync(USER_ID)

//        assert
        verify(usersCache, never()).cacheUser(any())
    }

    @Test
    fun test_whenUserIdIsSupplied_andUserIsReturned_shouldBeCachedInMemory() {
//        arrange
        success()

//        act
        systemUnderTest.fetchUserSync(USER_ID)

//        assert
        verify(usersCache).cacheUser(
            argThat { userId == USER_ID && username == USER_NAME }
        )
    }

    @Test
    fun test_whenUserIdIsSupplied_andItMatchesObjectInCache_shouldReturnOneFromCache() {
//        arrange
        whenever(usersCache.getUser(USER_ID))
            .thenReturn(User(USER_ID, USER_NAME))

//        act
        val cachedUser = systemUnderTest.fetchUserSync(USER_ID)

//        assert
        assertEquals(cachedUser.user?.userId, USER_ID)
        assertEquals(cachedUser.user?.username, USER_NAME)
    }

    // region helper methods
    private fun success() {
        whenever(fetchUserHttpEndpointSync.fetchUserSync(USER_ID))
            .thenReturn(FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.SUCCESS, USER_ID, USER_NAME
            ))
    }

    private fun generalError() {
        whenever(fetchUserHttpEndpointSync.fetchUserSync(USER_ID))
            .thenReturn(FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR, "", ""
            ))
    }

    private fun authError() {
        whenever(fetchUserHttpEndpointSync.fetchUserSync(USER_ID))
            .thenReturn(FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR, "", ""
            ))
    }

    private fun networkError() {
        whenever(fetchUserHttpEndpointSync.fetchUserSync(USER_ID))
            .thenThrow(NetworkErrorException())
    }
    // endregion helper methods


}