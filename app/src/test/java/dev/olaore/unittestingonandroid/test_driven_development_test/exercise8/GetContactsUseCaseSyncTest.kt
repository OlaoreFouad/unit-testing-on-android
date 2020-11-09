package dev.olaore.unittestingonandroid.test_driven_development_test.exercise8

import com.nhaarman.mockitokotlin2.*
import dev.olaore.unittestingonandroid.test_driven_development.exercise8.contacts.Contact
import dev.olaore.unittestingonandroid.test_driven_development.exercise8.networking.ContactSchema
import dev.olaore.unittestingonandroid.test_driven_development.exercise8.networking.GetContactsHttpEndpoint
import dev.olaore.unittestingonandroid.test_driven_development.solution_exercise_8.GetContactsUseCaseSync
import org.junit.Before
import org.junit.Test

const val ID = "contactId"
const val FULLNAME = "contactFullname"
const val IMAGEURL = "contactImageUrl"
const val FILTER_TERM = "custom"
const val PHONE_NUMBER = "08092929291"
const val AGE = 2.00
val MY_CONTACTS = listOf(
    Contact(ID, FULLNAME, IMAGEURL)
)

class GetContactsUseCaseSyncTest {

    lateinit var systemUnderTest: GetContactsUseCaseSync

    // region helper fields
    private lateinit var getContactsHttpEndpoint: GetContactsHttpEndpoint
    private lateinit var listener1: GetContactsUseCaseSync.Companion.Listener
    private lateinit var listener2: GetContactsUseCaseSync.Companion.Listener
    // endregion helper fields

    @Before
    fun setup() {
        getContactsHttpEndpoint = mock()
        systemUnderTest = GetContactsUseCaseSync(getContactsHttpEndpoint)
        success()
    }

    // 1. test that filterTerm is getting passed to the endpoint
    @Test
    fun test_whenGetContacts_isInvoked_shouldPassCorrectFilterTerm_toEndpoint() {
        // act
        systemUnderTest.getContactsSync(FILTER_TERM)
        // assert
        verify(getContactsHttpEndpoint).getContacts(
            argThat { this == FILTER_TERM }, any()
        )
    }

    // 2. test when request completes successfully, registered listeners should be notified with correct data
    @Test
    fun test_whenRequestCompletesSuccessfully_registeredListeners_shouldBeNotifiedWithCorrectData() {
        // arrange

        listener1 = mock()
        listener2 = mock()

        systemUnderTest.addListener(listener1)
        systemUnderTest.addListener(listener2)

        // act
        systemUnderTest.getContactsSync(FILTER_TERM)

        // assert
        verify(listener1).onContactsProvided(
            argThat { this == MY_CONTACTS }
        )
        verify(listener2).onContactsProvided(
            argThat { this == MY_CONTACTS }
        )
    }

    // 3. test when request completes successfully, unregistered listeners should not be notified
    @Test
    fun test_whenRequestCompletesSuccessfully_unregisteredListeners_shouldNotBeNotified() {
        // arrange

        listener1 = mock()
        listener2 = mock()

        systemUnderTest.addListener(listener1)
        systemUnderTest.addListener(listener2)

        systemUnderTest.removeListener(listener1)

        // act
        systemUnderTest.getContactsSync(FILTER_TERM)

        // assert
        verifyNoMoreInteractions(listener1)
        verify(listener2).onContactsProvided(
            argThat { this == MY_CONTACTS }
        )
    }

    // 4. test when request fails due to any error except network error, should return failure
    @Test
    fun test_whenRequestFails_dueToAnyError_shouldReturnFailure() {
        // arrange
        listener1 = mock()
        systemUnderTest.addListener(listener1)
        generalError()

        // act
        systemUnderTest.getContactsSync(FILTER_TERM)

        // assert
        verify(listener1).onErrorOccurred(
            argThat { this == GetContactsUseCaseSync.Companion.UseCaseResult.FAILURE }
        )
    }

    // 4. test when request fails due to a network error, should return failure
    @Test
    fun test_whenRequestFails_dueToNetworkError_shouldReturnFailure() {
        // arrange
        listener1 = mock()
        systemUnderTest.addListener(listener1)
        networkError()

        // act
        systemUnderTest.getContactsSync(FILTER_TERM)

        // assert
        verify(listener1).onErrorOccurred(
            argThat { this == GetContactsUseCaseSync.Companion.UseCaseResult.NETWORK_ERROR }
        )
    }

    // region helper methods

    private fun success() {
        whenever(getContactsHttpEndpoint.getContacts(argThat { this == FILTER_TERM }, any()))
            .doAnswer { invocationOnMock ->
                val arguments = invocationOnMock.arguments
                val callback: GetContactsHttpEndpoint.Callback = arguments[1] as GetContactsHttpEndpoint.Callback
                callback.onGetContactsSucceeded(getContactSchemas())
            }
    }

    private fun networkError() {
        whenever(getContactsHttpEndpoint.getContacts(any(), any()))
            .doAnswer { invocationOnMock ->
                val arguments = invocationOnMock.arguments
                val callback = arguments[1] as GetContactsHttpEndpoint.Callback
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR)
            }
    }

    private fun generalError() {
        whenever(getContactsHttpEndpoint.getContacts(any(), any()))
            .doAnswer { invocationOnMock ->
                val arguments = invocationOnMock.arguments
                val callback = arguments[1] as GetContactsHttpEndpoint.Callback
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR)
            }
    }

    private fun getContactSchemas(): MutableList<ContactSchema> {
        return mutableListOf<ContactSchema>(
            ContactSchema(ID, FULLNAME, PHONE_NUMBER, IMAGEURL, AGE)
        )
    }

    // endregion helper methods

}